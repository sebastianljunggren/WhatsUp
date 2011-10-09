package nu.placebo.whatsup.model;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;
import nu.placebo.whatsup.network.OperationResult;
import nu.placebo.whatsup.network.SessionTest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;



/**
 * Makes it possible to log in and stores session data.
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

	private void testSession() {
		SessionTest ts = new SessionTest(new SessionInfo(this.sessionName, this.sessionId));
		ts.addOperationListener(this);
		NetworkQueue.getInstance().add(ts);
		Log.w("WhatsUp", this.sessionId + this.sessionName);
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
	private void write() {
		Editor editor = context.getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
		editor.putString("userName", this.userName);
		editor.putString("password", this.password);
		editor.putString("sessionName", this.sessionName);
		editor.putString("sessionId", this.sessionId);
		editor.commit();
	}
	
	private void read() {
		SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
		this.userName = prefs.getString("userName", null);
		this.password = prefs.getString("password", null);
		this.sessionName =  prefs.getString("sessionName", null);
		this.sessionId =  prefs.getString("sessionId", null);
	}

	public void operationExcecuted(OperationResult<SessionInfo> result) {
		if(!result.hasErrors()) {
			Log.w("WhatsUp", "Session is valid");
		} else {
			Log.w("WhatsUp", "Session is not valid");
		}
		
	}
}
