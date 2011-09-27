package nu.placebo.whatsup.network;

import java.io.IOException;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.R.string;

import org.apache.commons.logging.Log;
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

	public static String retrieveAnnotationsInRange(double longitudeA,
			double latitudeA, double longitudeB, double latitudeB) {
		HttpGet request = new HttpGet(R.string.api_url + "nearby/" +longitudeA
				+ "," + latitudeA + "," + longitudeB + "," + latitudeB
				+ ".json");
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