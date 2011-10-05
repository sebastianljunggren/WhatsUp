package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Login extends AbstractNetworkOperation<OperationResult<Boolean>> {

	public void execute() {
		OperationResult<Boolean> operationResult = new OperationResult<Boolean>();
		DefaultHttpClient client = new DefaultHttpClient();

		// Try to get a session id to use when logging in.
		HttpPost request = new HttpPost(Constants.API_URL
				+ "system/connect.json");
		HttpResponse response = null;
		String result = null;
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			response = client.execute(request);
			operationResult.setStatusCode(response.getStatusLine()
					.getStatusCode());
			operationResult.setStatusMessage(response.getStatusLine()
					.getReasonPhrase());
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
		request = new HttpPost(Constants.API_URL + "user/login.json");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", "test"));
			nameValuePairs.add(new BasicNameValuePair("password", "WhatsUp!"));
			nameValuePairs.add(new BasicNameValuePair("sessid", sessid));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = client.execute(request);
			operationResult.setStatusCode(response.getStatusLine()
					.getStatusCode());
			operationResult.setStatusMessage(response.getStatusLine()
					.getReasonPhrase());
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.getConnectionManager().shutdown();
		super.notifyListeners(operationResult);

	}

}
