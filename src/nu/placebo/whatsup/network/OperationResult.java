package nu.placebo.whatsup.network;

public class OperationResult<T> implements IOperationResult<T> {

	private int statusCode;
	private String statusMessage;
	private boolean hasErrors;
	private T result;

	public int getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		
	}

	public boolean hasErrors() {
		return this.hasErrors;
	}

	public void setErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
		
	}

	public T getResult() {
		return this.result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
