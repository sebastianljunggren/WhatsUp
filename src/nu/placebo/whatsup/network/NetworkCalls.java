package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * Static methods for making calls to the server.
 * 
 */

public class NetworkCalls {

	/**
	 * Retrieves annotation marker data for a rectangular area.
	 * 
	 * @param longitudeA
	 * @param latitudeA
	 * @param longitudeB
	 * @param latitudeB
	 * @return
	 */
	public static String retrieveAnnotationMarkers(double latitudeA,
			double longitudeA, double latitudeB, double longitudeB) {
		return performGetRequest(Constants.API_URL + "nearby/" + latitudeA
				+ "," + longitudeA + "," + latitudeB + "," + longitudeB
				+ ".json");
	}

	/**
	 * Retrieves an annotation from the server
	 * 
	 * @param nid
	 *            the ID of the annotation
	 * @return
	 */
	public static String retrieveAnnotation(int nid) {
		return performGetRequest(Constants.API_URL + "node/" + nid + ".json");
	}

	private static String performGetRequest(String query) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(query);
		String result = null;
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			result = client.execute(request, handler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.getConnectionManager().shutdown();
		return result;
	}

	public static HttpResponse performPostRequest(String query,
			List<NameValuePair> body, List<NameValuePair> headers) throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(query);
		if (body != null) {
			request.setEntity(new UrlEncodedFormEntity(body));
		}
		HttpResponse response = client.execute(request);
		client.getConnectionManager().shutdown();
		return response;

	}
}