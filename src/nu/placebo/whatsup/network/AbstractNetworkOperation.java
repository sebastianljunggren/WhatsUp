package nu.placebo.whatsup.network;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNetworkOperation<T> implements
		NetworkOperation<T> {

	private List<NetworkOperationListener<T>> listeners = new ArrayList<NetworkOperationListener<T>>();

	public void addOperationListener(NetworkOperationListener<T> listener) {
		this.listeners.add(listener);
	}
	
	public void notifyListeners(T result) {
		for (NetworkOperationListener<T> listener: this.listeners){
			listener.operationExcecuted(result);
		}
	}

}
