package nu.placebo.whatsup.datahandling;

import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.OperationResult;

/**
 * This class holds the data gained from any possible local cache,
 * and by listening to it (through DataReturnListener), the new data
 * from the remote server can be extracted once it's retrieved by the
 * provider. This makes network calls - which can often take relatively
 * much time, or block for a number of reasons - not interfere with
 * the running of application.
 *
 * Classes getting an object of this type in return must register as listeners
 * to said object. To avoid any kind of problems where the server data is returned
 * before the class has registered as a listener, either through bad programming or
 * bad thread scheduling, this method will block its own listener method until there
 * is someone listening for it.
 *
 * @author Wange
 * @param <T> the type wanted as return.
 */
public class DataReturn<T> implements NetworkOperationListener<T> {

	private T localData;
	private OperationResult<T> serverData;
	private DataReturnListener listener;
	private boolean canFetchNewData = false;
	
	private int id;
	
	public DataReturn(T localData, int id) {
		this.localData = localData;
		this.id = id;
	}
	
	/**
	 * Gets the locally returned data. If this is null, it means no
	 * local data was found and the owner of this DataReturn object
	 * will have to wait for their DataReturnListener-method triggering.
	 * 
	 * @return the data acquired locally. Null if nothing found.
	 */
	public T getLocalData() {
		return localData;
	}
	
	/**
	 * Gets the new data fetched from the server. Must be called
	 * after this object has called the listener. If called before,
	 * null will be returned.
	 * 
	 * @return the new data, null if called before the listener call,
	 * or if this method is called despite the listener call returning
	 * false.
	 */
	public OperationResult <T> getNewData() {
		return (canFetchNewData ? serverData : null);
	}
	
	public void addDataReturnListener(DataReturnListener listener) {
		if(this.listener == null) {
			this.listener = listener;
			notifyAll();
		}
	}

	public void operationExcecuted(OperationResult<T> result) {
		boolean threadInterupted = false;
		while(this.listener == null || threadInterupted) {
			try {
				wait();
			} catch (InterruptedException e) {
				threadInterupted = true;
			}
		}
		if(!threadInterupted) {
			if(result != null && result.getResult() != null &&
					!localData.equals(result.getResult())) {
				this.serverData = result;
				canFetchNewData  = true;
				listener.newDataReceived(true);
				DataProvider.getDataProvider(null).newDataRecieved(true, id);
			} else {
				listener.newDataReceived(false);
				DataProvider.getDataProvider(null).newDataRecieved(false, id);
			}
		}
	}
}
