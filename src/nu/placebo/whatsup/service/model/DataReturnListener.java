package nu.placebo.whatsup.service.model;

public interface DataReturnListener {
	
	/**
	 * Called when the DataReturn object has received the 
	 * new data. After this is called once, the DataReturn object
	 * will not call it again, and may be discarded.
	 * 
	 * @param dataIsNew whether the data differs from the local data
	 * or not.
	 */
	public void newDataReceived(boolean dataIsNew);
}
