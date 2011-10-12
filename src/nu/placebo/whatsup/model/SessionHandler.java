package nu.placebo.whatsup.model;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.network.Action;
import nu.placebo.whatsup.network.Login;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;
import nu.placebo.whatsup.network.OperationResult;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * Stores session data and handles automatic log in and log off.
 * 
 */

public class SessionHandler implements NetworkOperationListener<SessionInfo> {

	private static SessionHandler instance;
	private static Context context;
	private String userName;
	private String password;
	private String sessionName;
	private String sessionId;

	private SessionHandler() {
		this.read();
	}

	public void testSession() {
		if (this.hasSession()) {
//			SessionTest ts = new SessionTest(new SessionInfo(this.sessionName,
//					this.sessionId));
//			ts.addOperationListener(this);
//			NetworkQueue.getInstance().add(ts);
//		} else {
//			Log.w("WhatsUp", "No session to test.");
		}
	}

	private boolean hasSession() {
		return this.sessionId != null && this.sessionName != null;
	}

	public static SessionHandler getInstance(Context c) {
		if (instance == null) {
			context = c;
			instance = new SessionHandler();
		}
		return instance;
	}

	public void saveCredentials(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.write();
	}

	public void saveSession(SessionInfo sessionInfo) {
		this.sessionName = sessionInfo.getSessionName();
		this.sessionId = sessionInfo.getSessionId();
		this.write();
	}

	public SessionInfo getSession() {
		return new SessionInfo(this.sessionName, this.sessionId);
	}

	public String getUserName() {
		return this.userName;
	}

	private void write() {
		Editor editor = context.getSharedPreferences(
				Constants.PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString("userName", this.userName);
		editor.putString("password", this.password);
		editor.putString("sessionName", this.sessionName);
		editor.putString("sessionId", this.sessionId);
		editor.commit();
	}

	private void read() {
		SharedPreferences prefs = context.getSharedPreferences(
				Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
		this.userName = prefs.getString("userName", null);
		this.password = prefs.getString("password", null);
		this.sessionName = prefs.getString("sessionName", null);
		this.sessionId = prefs.getString("sessionId", null);
	}

	public void operationExcecuted(OperationResult<SessionInfo> result) {
		if (result.getAction() == Action.LOG_IN && !result.hasErrors()) {
			this.saveSession(result.getResult());
		} else if (result.getAction() == Action.TEST_SESSION) {
			if (result.hasErrors() && this.hasCredentials()) {
				this.attemptLogIn();
			}
		}
	}

	private void attemptLogIn() {
		Login logIn = new Login(this.userName, this.password);
		logIn.addOperationListener(this);
		NetworkQueue.getInstance().add(logIn);
	}

	public boolean hasCredentials() {
		return this.userName != null && this.password != null;
	}
}
