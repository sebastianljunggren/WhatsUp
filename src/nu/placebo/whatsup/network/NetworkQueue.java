package nu.placebo.whatsup.network;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import nu.placebo.whatsup.constants.Constants;

/**
 * 
 * This class makes sure not to many service calls are running and creates
 * threads to run calls in. The class is a singleton.
 * 
 */

public final class NetworkQueue {
	private static NetworkQueue instance;
	private AbstractQueue<NetworkOperation<?>> queue = new ConcurrentLinkedQueue<NetworkOperation<?>>();
	private int activeCalls = 0;

	/**
	 * Private constructor to ensure that the class is singleton.
	 */
	private NetworkQueue() {}

	/**
	 * Gives access to the instance of the NetworkQueue.
	 * 
	 * @return
	 */
	public static NetworkQueue getInstance() {
		if (instance == null) {
			instance = new NetworkQueue();
		}
		return instance;
	}

	public void add(NetworkOperation<?> item){
		this.queue.add(item);
		if(canStartNewCall()) {
			nextCall();
		}
	}

	private void nextCall() {
		if(!this.queue.isEmpty()) {
			this.activeCalls++;
			new Thread(new Runnable() {
				public void run() {       
					queue.poll().execute();
					activeCalls--;
					itemFinished();
				}
			}).start();
		}
	}

	private synchronized void itemFinished() {
		if(canStartNewCall()) {
			nextCall();
		}
	}
	
	private boolean canStartNewCall() {
		return this.activeCalls < Constants.ALLOWED_CONCURRENT_CALLS;
	}
}
