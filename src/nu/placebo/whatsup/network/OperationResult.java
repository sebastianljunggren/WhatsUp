package nu.placebo.whatsup.network;

/**
 * 
 * Data carrier with information about the result of a NetworkOperation.
 * 
 */

public class OperationResult<T> {

	private int statusCode;
	private String statusMessage;
	private boolean hasErrors;
	private T result;
	private Action action;

	public OperationResult(boolean hasErrors, int statusCode,
			String statusMessage, T result) {
		this(hasErrors, statusCode, statusMessage, result, Action.DEFAULT);
	}

	public OperationResult(boolean hasErrors, int statusCode,
			String statusMessage, T result, Action action) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.hasErrors = hasErrors;
		this.result = result;
		this.action = action;
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

	public Action getAction() {
		return this.action;
	}

}
