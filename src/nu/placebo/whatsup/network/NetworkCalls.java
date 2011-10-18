package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.List;

import nu.placebo.whatsup.model.SessionInfo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.net.http.AndroidHttpClient;

/**
 * 
 * Static methods for making calls to the server.
 * 
 */

public class NetworkCalls {

	public static HttpResponse performGetRequest(String query) {
		HttpClient client = AndroidHttpClient.newInstance("");
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
		HttpClient client = AndroidHttpClient.newInstance("");
		HttpPost request = new HttpPost(query);

		HttpResponse response = null;
		try {
			if (body != null) {
				AbstractHttpEntity ent = new UrlEncodedFormEntity(body,
						HTTP.UTF_8);
				request.setEntity(ent);
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