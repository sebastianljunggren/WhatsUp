package nu.placebo.whatsup.network;

public interface IOperationResult<T> {
	public int getStatusCode();

	public String getStatusMessage();

	public boolean hasErrors();

	public T getResult();
}
