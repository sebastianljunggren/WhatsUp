package nu.placebo.whatsup.network;

import java.io.IOException;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.Annotation;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;

import com.google.android.maps.GeoPoint;

/**
 * 
 * Retrieves an Annotation from the server.
 *
 */

public class AnnotationCreate extends AbstractNetworkOperation<Annotation> { // TODO: Check if this is a good idea

	private String author, title, body;
	private GeoPoint pos;
	
	
	/**
	 * 
	 * @param author
	 * @param title
	 * @param body
	 * @param pos
	 */
	public AnnotationCreate(String author, String title, String body, GeoPoint pos) {
		this.author = author;
		this.title = title;
		this.body = body;
		this.pos = pos;
	}

	public void execute() {
		Annotation annotation = null;
		HttpResponse response = null;
		boolean hasErrors = true;
		try {
		/*	response = NetworkCalls.performGetRequest(Constants.API_URL
					+ "node/" + this.nid + ".json"); */ // TODO: Complete json query
			ResponseHandler<String> handler = new BasicResponseHandler();
			annotation = new Annotation(handler.handleResponse(response));
			hasErrors = false;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.notifyListeners(new OperationResult<Annotation>(hasErrors,
				response.getStatusLine().getStatusCode(), response
						.getStatusLine().getReasonPhrase(), annotation));
	}
}