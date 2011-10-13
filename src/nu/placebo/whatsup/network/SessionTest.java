package nu.placebo.whatsup.network;

import java.io.IOException;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.SessionInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 
 * This class tests whether a session is active or if the user needs to log in
 * again.
 * 
 */

public class SessionTest extends AbstractNetworkOperation<SessionInfo> {

	private SessionInfo sessionInfo;

	public SessionTest(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public OperationResult<SessionInfo> execute() {
		HttpResponse response = NetworkCalls.performPostRequest(
				Constants.API_URL + "system/connect.json", null, sessionInfo);
		ResponseHandler<String> handler = new BasicResponseHandler();
		String result = null;
		try {
			result = handler.handleResponse(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean hasErrors = this.parseResult(result);
		if (response != null) {
			if (hasErrors) {
				sessionInfo = null;
			}
			return new OperationResult<SessionInfo>(hasErrors,
					response.getStatusLine().getStatusCode(), response
							.getStatusLine().getReasonPhrase(), sessionInfo,
					Action.TEST_SESSION);
		} else {
			return new OperationResult<SessionInfo>(hasErrors,
					0, "Problems with the network", null, Action.TEST_SESSION);
		}
	}

	private boolean parseResult(String result) {
		boolean hasErrors = true;
		if (result != null) {
			try {
				JSONObject json = new JSONObject(result);
				int uid = json.getJSONObject("user").getInt("uid");
				if (uid != 0) {
					hasErrors = false;
					Log.w("WhatsUp", "Session " + sessionInfo.getSessionName()
							+ " is valid");
				}

			} catch (JSONException e) {
				Log.w("WhatsUp", "Session " + sessionInfo.getSessionName()
						+ " is invalid");
				e.printStackTrace();
			}
		}
		return hasErrors;
	}

}
