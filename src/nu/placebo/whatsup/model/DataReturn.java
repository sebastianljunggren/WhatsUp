package nu.placebo.whatsup.model;

/**
 * This class holds the data gained from any possible local cache,
 * and by listening to it (through DataReturnListener), the new data
 * from the remote server can be extracted once it's retrieved by the
 * provider. This makes network calls - which can often take relatively
 * much time, or block for a number of reasons - not interfere with
 * the running of application.
 *
 * Classes getting an object of this type in return must instantly register
 * themselves as listeners for it, or else they might miss the server data.
 *
 * @author Wange
 * @param <T> the type wanted as return.
 */
public class DataReturn<T> {

	private T localData;
	private T serverData;
	private DataReturnListener listener;
	private boolean canFetchNewData = false;
	
	public DataReturn(T localData) {
		this.localData = localData;
	}
	
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
	public T getNewData() {
		return (canFetchNewData ? serverData : null);
	}
	
	void fillInServerData(T serverData) throws NoListenerException {
		if(this.listener == null) {
			throw new NoListenerException();
		}
		if(!localData.equals(serverData)) {
			this.serverData = serverData;
			canFetchNewData  = true;
			listener.newDataReceived(true);
		} else {
			listener.newDataReceived(false);
		}
	}
	
	public void addDataReturnListener(DataReturnListener listener) {
		if(this.listener == null) {
			this.listener = listener;
		}
	}
}
