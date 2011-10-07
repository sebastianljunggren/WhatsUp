package nu.placebo.whatsup.network;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Abstract class that helps NetworkOperations to handle their listeners.
 *
 */
public abstract class AbstractNetworkOperation<T> implements
		NetworkOperation<T> {

	private List<NetworkOperationListener<T>> listeners = new ArrayList<NetworkOperationListener<T>>();

	public final void addOperationListener(NetworkOperationListener<T> listener) {
		this.listeners.add(listener);
	}

	public final void notifyListeners(OperationResult<T> result) {
		for (NetworkOperationListener<T> listener : this.listeners) {
			listener.operationExcecuted(result);
		}
	}

}
