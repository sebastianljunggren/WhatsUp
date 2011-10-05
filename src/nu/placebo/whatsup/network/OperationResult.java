package nu.placebo.whatsup.network;

public class OperationResult<T> implements IOperationResult<T> {

	private int statusCode;
	private String statusMessage;
	private boolean hasErrors;
	private T result;

	public OperationResult(boolean hasErrors, int statusCode,
			String statusMessage, T result) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.hasErrors = hasErrors;
		this.result = result;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public boolean hasErrors() {
		return this.hasErrors;
	}

	public T getResult() {
		return this.result;
	}

}
