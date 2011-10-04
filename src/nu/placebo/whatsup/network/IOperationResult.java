package nu.placebo.whatsup.network;

public interface IOperationResult<T> {
	public int getStatusCode();

	public void setStatusCode(int statusCode);

	public String getStatusMessage();

	public void setStatusMessage(String statusMessage);

	public boolean hasErrors();

	public void setErrors(boolean hasErrors);

	public T getResult();
	
	public void setResult(T result);
}
