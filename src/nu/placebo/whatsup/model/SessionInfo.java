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

	public boolean equals(Object o) {
		if (o instanceof SessionInfo) {
			SessionInfo si = (SessionInfo) o;
			return this.getSessionId().equals(si.getSessionId())
					&& this.getSessionName().equals(si.getSessionName());
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return sessionId.hashCode() + sessionName.hashCode();
	}
}
