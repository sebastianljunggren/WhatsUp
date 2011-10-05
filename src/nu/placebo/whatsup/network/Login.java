package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Login extends AbstractNetworkOperation<IOperationResult<Boolean>> {

	private String username;
	private String password;

	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void execute() {
		boolean hasErrors = true;
		// Try to get a session id to use when logging in.
		HttpResponse response = null;
		String result = null;
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			response = NetworkCalls.performPostRequest(Constants.API_URL
				+ "system/connect.json", null, null);
			result = handler.handleResponse(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String sessid = null;
		try {
			JSONObject json = new JSONObject(result);
			sessid = json.getString("sessid");
			Log.w("WhatsUp", "sessid: " + sessid);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Actually log in
		try {
			List<NameValuePair> body = new ArrayList<NameValuePair>(2);
			body.add(new BasicNameValuePair("username", this.username));
			body.add(new BasicNameValuePair("password", this.password));
			body.add(new BasicNameValuePair("sessid", sessid));
			response = NetworkCalls.performPostRequest(Constants.API_URL + "user/login.json", body, null);
			result = handler.handleResponse(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String sessionName = null;
		try {
			JSONObject json = new JSONObject(result);
			sessid = json.getString("sessid");
			Log.w("WhatsUp", "Sessid: " + sessid);
			sessionName = json.getString("session_name");
			Log.w("WhatsUp", "Session name: " + sessionName);
			json = json.getJSONObject("user");
			Log.w("WhatsUp", "User " + json.getString("name") + " with uid "
					+ json.getString("uid") + " logged in.");
			hasErrors  = false;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		super.notifyListeners(new OperationResult<Boolean>(hasErrors, response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), !hasErrors));
	}
}
