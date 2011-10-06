package nu.placebo.whatsup.model;

public class SessionInfo {
	private String sessionName;
	private String sessionId;

	/**
	 * Contains the session data required to authenticate after a successful log
	 * in.
	 */
	public SessionInfo(String sessionName, String sessionId) {
		this.sessionName = sessionName;
		this.sessionId = sessionId;
	}

	public String getSessionName() {
		return this.sessionName;
	}

	public String getSessionId() {
		return this.sessionId;
	}
}
