package nu.placebo.whatsup.network;

/**
 * 
 * Defines a common interface for network operation so they can be used in the
 * NetworkQueue.
 * 
 */

public interface NetworkOperation<T> {
	public void addOperationListener(NetworkOperationListener<T> listener);
	public void notifyListeners(OperationResult<T> result);
	public OperationResult<T> execute();
	public OperationResult<T> getResult();
	public void setOperationResult(OperationResult<T> result);
}
