package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
			List<NameValuePair> body, List<NameValuePair> headers) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(query);

		HttpResponse response = null;
		try {
			if (body != null) {
				request.setEntity(new UrlEncodedFormEntity(body));
			}
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.getConnectionManager().shutdown();
		return response;

	}
}