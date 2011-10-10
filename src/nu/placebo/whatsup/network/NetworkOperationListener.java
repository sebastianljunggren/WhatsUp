package nu.placebo.whatsup.network;

/**
 * 
 * Class that needs to be implemented in order to be notified of the result of a
 * NetworkOperation. Also the listener has to be added a listener of the
 * operation it is interested in.
 * 
 */

public interface NetworkOperationListener<T> {
	public void operationExcecuted(OperationResult<T> result);
}