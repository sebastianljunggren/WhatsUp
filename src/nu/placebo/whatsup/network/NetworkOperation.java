package nu.placebo.whatsup.network;

/**
 * 
 * Defines a common interface for network operation so they can be used in the
 * NetworkQueue.
 * 
 */

public interface NetworkOperation<T> {
	public void addOperationListener(NetworkOperationListener<T> listener);

	public void execute();
}
