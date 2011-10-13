package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.SessionInfo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Handles the necessary network calls to log in.
 */
public class Login extends AbstractNetworkOperation<SessionInfo> {

	private String username;
	private String password;
	private String sessionId;
	private String sessionName;
	private boolean hasErrors;

	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public OperationResult<SessionInfo> execute() {
		HttpResponse response = null;
		String result = null;
		this.hasErrors = true;
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			// Try to get a session id to use when logging in.
			response = NetworkCalls.performPostRequest(Constants.API_URL
					+ "system/connect.json", null, null);
			result = handler.handleResponse(response);
			this.parseConnect(result);

			// Actually send the user name and password to the server.
			List<NameValuePair> body = new ArrayList<NameValuePair>(2);
			body.add(new BasicNameValuePair("username", this.username));
			body.add(new BasicNameValuePair("password", this.password));
			body.add(new BasicNameValuePair("sessid", this.sessionId));
			response = NetworkCalls.performPostRequest(Constants.API_URL
					+ "user/login.json", body, null);
			result = handler.handleResponse(response);
			this.parseLogIn(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null) {
			return new OperationResult<SessionInfo>(
					this.hasErrors, response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase(),
					new SessionInfo(this.sessionName, this.sessionId),
					Action.LOG_IN);
		} else {
			return new OperationResult<SessionInfo>(hasErrors,
					1337, "Problems with the network", null, Action.LOG_IN);
		}
	}

	private void parseConnect(String result) {
		try {
			JSONObject json = new JSONObject(result);
			this.sessionId = json.getString("sessid");
			Log.w("WhatsUp", "Connected with session ID " + this.sessionId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void parseLogIn(String result) {
		try {
			JSONObject json = new JSONObject(result);
			this.sessionId = json.getString("sessid");
			sessionName = json.getString("session_name");
			Log.w("WhatsUp", "Logged in with session name " + sessionName
					+ " and session ID: " + this.sessionId);
			json = json.getJSONObject("user");
			Log.w("WhatsUp", "User " + json.getString("name") + " with uid "
					+ json.getString("uid") + " logged in.");
			this.hasErrors = false;
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}