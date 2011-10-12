package nu.placebo.whatsup.network;

import nu.placebo.whatsup.android.os.AsyncTask;

/**
 * 
 * Makes it possible to run NetworkOperation in their own thread and then easily
 * return it to the UI thread.
 * 
 */

public class NetworkTask<T> extends
		AsyncTask<NetworkOperation<T>, NetworkOperation<T>> {

	@Override
	protected NetworkOperation<T> doInBackground(NetworkOperation<T> o) {
		o.setOperationResult(o.execute());
		return o;
	}

	@Override
	protected void onPostExecute(NetworkOperation<T> o) {
		o.notifyListeners(o.getResult());
	}

}
