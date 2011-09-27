package nu.placebo.whatsup.network;

import java.io.IOException;

import nu.placebo.whatsup.R;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import android.net.http.AndroidHttpClient;

/**
 * 
 * Static methods for making calls to the server.
 * 
 */

public class NetworkCalls {
	private static final AndroidHttpClient client = AndroidHttpClient
			.newInstance("");

	/**
	 * Retrieves annotation marker data for a rectangular area.
	 * 
	 * @param longitudeA
	 * @param latitudeA
	 * @param longitudeB
	 * @param latitudeB
	 * @return
	 */
	public static String retrieveAnnotationsInRange(double latitudeA,
			double longitudeA, double latitudeB, double longitudeB) {
		return performGetRequest(R.string.api_url + "nearby/" + latitudeA + ","
				+ longitudeA + "," + latitudeB + "," + longitudeB + ".json");
	}

	/**
	 * Retrieves an annotation from the server
	 * 
	 * @param nid
	 *            the ID of the annotation
	 * @return
	 */
	public static String retrieveAnnotation(int nid) {
		return performGetRequest(R.string.api_url + nid + ".json");
	}

	private static String performGetRequest(String query) {
		HttpGet request = new HttpGet("query");
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
}