package nu.placebo.whatsup.network;

public interface NetworkOperation<T> {
	public void addOperationListener(NetworkOperationListener<T> listener);
	public void execute();
}
