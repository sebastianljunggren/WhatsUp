package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.SessionInfo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Handles the necessary network calls to log in.
 */
public class Login extends
		AbstractNetworkOperation<SessionInfo> {

	private String username;
	private String password;
	private String sessionId;
	private String sessionName;
	private boolean hasErrors;

	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void execute() {
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

		super.notifyListeners(new OperationResult<SessionInfo>(this.hasErrors,
				response.getStatusLine().getStatusCode(), response
						.getStatusLine().getReasonPhrase(), new SessionInfo(
						null, null)));
	}

	private void parseConnect(String result) {
		try {
			JSONObject json = new JSONObject(result);
			this.sessionId = json.getString("sessid");
			Log.w("WhatsUp", "sessid: " + this.sessionId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void parseLogIn(String result) {
		try {
			JSONObject json = new JSONObject(result);
			this.sessionId = json.getString("sessid");
			Log.w("WhatsUp", "Sessid: " + this.sessionId);
			sessionName = json.getString("session_name");
			Log.w("WhatsUp", "Session name: " + sessionName);
			json = json.getJSONObject("user");
			Log.w("WhatsUp", "User " + json.getString("name") + " with uid "
					+ json.getString("uid") + " logged in.");
			this.hasErrors = false;
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
