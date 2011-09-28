package nu.placebo.whatsup.network;

public interface NetworkOperationListener<T> {
	public void operationExcecuted(T result);
}
