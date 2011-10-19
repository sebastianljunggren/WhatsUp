package nu.placebo.whatsup.android.os;

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Message;
import android.os.Process;

/**
 * 
 * Android source code adapted to our needs. Removed varargs to avoid warnings
 * when passing generic types into the execute(). Also removed the unneeded step
 * (for us) of posting a progress update.
 * 
 * The warnings in this file existed before we adapted it.
 * 
 */

public abstract class AsyncTask<Params, Result> {
	private static final String LOG_TAG = "AsyncTask";

	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_POOL_SIZE = 128;
	private static final int KEEP_ALIVE = 10;

	private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(
			10);

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};

	private static final ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(
			CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			sWorkQueue, sThreadFactory);

	private static final int MESSAGE_POST_RESULT = 0x1;
	private static final int MESSAGE_POST_CANCEL = 0x3;

	private static final InternalHandler sHandler = new InternalHandler();

	private final WorkerRunnable<Params, Result> mWorker;
	private final FutureTask<Result> mFuture;

	private volatile Status mStatus = Status.PENDING;

	/**
	 * Indicates the current status of the task. Each status will be set only
	 * once during the lifetime of a task.
	 */
	public enum Status {
		/**
		 * Indicates that the task has not been executed yet.
		 */
		PENDING,
		/**
		 * Indicates that the task is running.
		 */
		RUNNING,
		/**
		 * Indicates that {@link AsyncTask#onPostExecute} has finished.
		 */
		FINISHED,
	}

	/**
	 * Creates a new asynchronous task. This constructor must be invoked on the
	 * UI thread.
	 */
	public AsyncTask() {
		mWorker = new WorkerRunnable<Params, Result>() {
			public Result call() throws Exception {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				return doInBackground(mParams);
			}
		};

		mFuture = new FutureTask<Result>(mWorker) {
			@Override
			protected void done() {
				Message message;
				Result result = null;

				try {
					result = super.get();
				} catch (InterruptedException e) {
					android.util.Log.w(LOG_TAG, e);
				} catch (ExecutionException e) {
					throw new RuntimeException(
							"An error occured while executing doInBackground()",
							e.getCause());
				} catch (CancellationException e) {
					message = sHandler.obtainMessage(MESSAGE_POST_CANCEL,
							new AsyncTaskResult<Result>(AsyncTask.this,
									(Result[]) null));
					message.sendToTarget();
					return;
				} catch (Throwable t) {
					throw new RuntimeException(
							"An error occured while executing "
									+ "doInBackground()", t);
				}

				message = sHandler.obtainMessage(MESSAGE_POST_RESULT,
						new AsyncTaskResult<Result>(AsyncTask.this, result));
				message.sendToTarget();
			}
		};
	}

	/**
	 * Returns the current status of this task.
	 * 
	 * @return The current status.
	 */
	public final Status getStatus() {
		return mStatus;
	}

	/**
	 * Override this method to perform a computation on a background thread. The
	 * specified parameters are the parameters passed to {@link #execute} by the
	 * caller of this task.
	 * 
	 * This method can call {@link #publishProgress} to publish updates on the
	 * UI thread.
	 * 
	 * @param params
	 *            The parameters of the task.
	 * 
	 * @return A result, defined by the subclass of this task.
	 * 
	 * @see #onPreExecute()
	 * @see #onPostExecute
	 * @see #publishProgress
	 */
	protected abstract Result doInBackground(Params params);

	/**
	 * Runs on the UI thread before {@link #doInBackground}.
	 * 
	 * @see #onPostExecute
	 * @see #doInBackground
	 */
	protected void onPreExecute() {
	}

	/**
	 * Runs on the UI thread after {@link #doInBackground}. The specified result
	 * is the value returned by {@link #doInBackground} or null if the task was
	 * cancelled or an exception occured.
	 * 
	 * @param result
	 *            The result of the operation computed by
	 *            {@link #doInBackground}.
	 * 
	 * @see #onPreExecute
	 * @see #doInBackground
	 */
	@SuppressWarnings({})
	protected void onPostExecute(Result result) {
	}

	/**
	 * Runs on the UI thread after {@link #cancel(boolean)} is invoked.
	 * 
	 * @see #cancel(boolean)
	 * @see #isCancelled()
	 */
	protected void onCancelled() {
	}

	/**
	 * Returns <tt>true</tt> if this task was cancelled before it completed
	 * normally.
	 * 
	 * @return <tt>true</tt> if task was cancelled before it completed
	 * 
	 * @see #cancel(boolean)
	 */
	public final boolean isCancelled() {
		return mFuture.isCancelled();
	}

	/**
	 * Attempts to cancel execution of this task. This attempt will fail if the
	 * task has already completed, already been cancelled, or could not be
	 * cancelled for some other reason. If successful, and this task has not
	 * started when <tt>cancel</tt> is called, this task should never run. If
	 * the task has already started, then the <tt>mayInterruptIfRunning</tt>
	 * parameter determines whether the thread executing this task should be
	 * interrupted in an attempt to stop the task.
	 * 
	 * @param mayInterruptIfRunning
	 *            <tt>true</tt> if the thread executing this task should be
	 *            interrupted; otherwise, in-progress tasks are allowed to
	 *            complete.
	 * 
	 * @return <tt>false</tt> if the task could not be cancelled, typically
	 *         because it has already completed normally; <tt>true</tt>
	 *         otherwise
	 * 
	 * @see #isCancelled()
	 * @see #onCancelled()
	 */
	public final boolean cancel(boolean mayInterruptIfRunning) {
		return mFuture.cancel(mayInterruptIfRunning);
	}

	/**
	 * Waits if necessary for the computation to complete, and then retrieves
	 * its result.
	 * 
	 * @return The computed result.
	 * 
	 * @throws CancellationException
	 *             If the computation was cancelled.
	 * @throws ExecutionException
	 *             If the computation threw an exception.
	 * @throws InterruptedException
	 *             If the current thread was interrupted while waiting.
	 */
	public final Result get() throws InterruptedException, ExecutionException {
		return mFuture.get();
	}

	/**
	 * Waits if necessary for at most the given time for the computation to
	 * complete, and then retrieves its result.
	 * 
	 * @param timeout
	 *            Time to wait before cancelling the operation.
	 * @param unit
	 *            The time unit for the timeout.
	 * 
	 * @return The computed result.
	 * 
	 * @throws CancellationException
	 *             If the computation was cancelled.
	 * @throws ExecutionException
	 *             If the computation threw an exception.
	 * @throws InterruptedException
	 *             If the current thread was interrupted while waiting.
	 * @throws TimeoutException
	 *             If the wait timed out.
	 */
	public final Result get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return mFuture.get(timeout, unit);
	}

	/**
	 * Executes the task with the specified parameters. The task returns itself
	 * (this) so that the caller can keep a reference to it.
	 * 
	 * This method must be invoked on the UI thread.
	 * 
	 * @param params
	 *            The parameters of the task.
	 * 
	 * @return This instance of AsyncTask.
	 * 
	 * @throws IllegalStateException
	 *             If {@link #getStatus()} returns either
	 *             {@link AsyncTask.Status#RUNNING} or
	 *             {@link AsyncTask.Status#FINISHED}.
	 */
	public final AsyncTask<Params, Result> execute(Params params) {
		if (mStatus != Status.PENDING) {
			switch (mStatus) {
			case RUNNING:
				throw new IllegalStateException("Cannot execute task:"
						+ " the task is already running.");
			case FINISHED:
				throw new IllegalStateException("Cannot execute task:"
						+ " the task has already been executed "
						+ "(a task can be executed only once)");
			}
		}

		mStatus = Status.RUNNING;

		onPreExecute();

		mWorker.mParams = params;
		sExecutor.execute(mFuture);

		return this;
	}

	private void finish(Result result) {
		if (isCancelled())
			result = null;
		onPostExecute(result);
		mStatus = Status.FINISHED;
	}

	private static class InternalHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			AsyncTaskResult result = (AsyncTaskResult) msg.obj;
			switch (msg.what) {
			case MESSAGE_POST_RESULT:
				// There is only one result
				result.mTask.finish(result.mData[0]);
				break;
			case MESSAGE_POST_CANCEL:
				result.mTask.onCancelled();
				break;
			}
		}
	}

	private static abstract class WorkerRunnable<Params, Result> implements
			Callable<Result> {
		Params mParams;
	}

	private static class AsyncTaskResult<Data> {
		final AsyncTask mTask;
		final Data[] mData;

		AsyncTaskResult(AsyncTask task, Data... data) {
			mTask = task;
			mData = data;
		}
	}
}