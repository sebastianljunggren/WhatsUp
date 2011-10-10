package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.List;

import nu.placebo.whatsup.model.SessionInfo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.util.Log;

/**
 * 
 * Static methods for making calls to the server.
 * 
 */

public class NetworkCalls {

	public static HttpResponse performGetRequest(String query) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(query);
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.getConnectionManager().shutdown();
		return response;
	}

	public static HttpResponse performPostRequest(String query,
			List<NameValuePair> body, SessionInfo sessionInfo) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(query);

		HttpResponse response = null;
		try {
			if (body != null) {
				request.setEntity(new UrlEncodedFormEntity(body));
			}
			if (sessionInfo != null) {
				request.addHeader(new BasicHeader("Cookie", sessionInfo
						.getSessionName() + "=" + sessionInfo.getSessionId()));
			}
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.getConnectionManager().shutdown();
		return response;

	}
}