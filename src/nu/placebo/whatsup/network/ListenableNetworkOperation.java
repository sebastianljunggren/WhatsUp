package nu.placebo.whatsup.network;

public interface ListenableNetworkOperation<T> extends NetworkOperation {
	public void addOperationListener(NetworkOperationListener<T> listener);
}
