package nu.placebo.whatsup.model;

import java.util.Observable;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.network.Action;
import nu.placebo.whatsup.network.Login;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkTask;
import nu.placebo.whatsup.network.OperationResult;
import nu.placebo.whatsup.network.SessionTest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * Stores session data and handles automatic log in and log off.
 * 
 */

public class SessionHandler extends Observable implements NetworkOperationListener<SessionInfo> {

	private static SessionHandler instance;
	private static Context context;
	private String userName;
	private String password;
	private String sessionName;
	private String sessionId;
	private boolean loggedIn = false;

	private SessionHandler() {
		this.read();
	}

	public void testSession() {
		if (this.hasSession()) {
			SessionTest ts = new SessionTest(new SessionInfo(this.sessionName,
					this.sessionId));
			ts.addOperationListener(this);
			new NetworkTask<SessionInfo>().execute(ts);
		} else {
			Log.w("WhatsUp", "No session to test.");
		}
	}

	public boolean hasSession() {
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
		this.setChanged();
		this.notifyObservers();
	}

	public void saveSession(SessionInfo sessionInfo) {
		this.sessionName = sessionInfo.getSessionName();
		this.sessionId = sessionInfo.getSessionId();
		this.write();
		this.setChanged();
		this.notifyObservers();
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
			Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show();
			this.loggedIn = true;
			this.setChanged();
		} else if (result.getAction() == Action.TEST_SESSION) {
			if (!result.hasErrors()){
				Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show();
				this.loggedIn = true;
				this.setChanged();
			} else if (result.hasErrors() && this.hasCredentials()) {
				this.login();
				Log.w("WhatsUp", "Trying to log in");
			}
		}
		Log.w("WhatsUp", "Stuff");
	}

	public void login() {
		Login logIn = new Login(this.userName, this.password);
		logIn.addOperationListener(this);
		new NetworkTask<SessionInfo>().execute(logIn);
		Log.w("WhatsUp", "Trying to log in");
	}

	public boolean hasCredentials() {
		return this.userName != null && this.password != null;
	}

	public void logOut() {
		this.saveSession(new SessionInfo(null, null));
		this.loggedIn = false;
		// TODO Actually log out from the server.
		Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT);
	}
	
	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	public void reset() {
		this.saveCredentials(null, null);
		this.saveSession(new SessionInfo(null, null));
		this.deleteObservers();
	}
}
