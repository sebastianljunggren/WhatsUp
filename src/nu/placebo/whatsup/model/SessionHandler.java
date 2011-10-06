package nu.placebo.whatsup.model;

/**
 * Makes it possible to log in and stores session data.
 */

public class SessionHandler {

	private static SessionHandler instance;

	private SessionHandler() {
		// empty private constructor to ensure the class is a singleton
	}

	public static SessionHandler getInstance() {
		if (instance == null) {
			instance = new SessionHandler();
		}
		return instance;
	}

}
