package nu.placebo.whatsup.network;

import java.io.IOException;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.Annotation;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;

/**
 * 
 * Retrieves an Annotation from the server.
 *
 */

public class AnnotationRetrieve extends AbstractNetworkOperation<Annotation> {

	private int nid;

	public AnnotationRetrieve(int nid) {
		this.nid = nid;
	}

	public void execute() {
		Annotation annotation = null;
		HttpResponse response = null;
		boolean hasErrors = true;
		try {
			response = NetworkCalls.performGetRequest(Constants.API_URL
					+ "node/" + this.nid + ".json");
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
		
		if (response != null) {
			super.notifyListeners(new OperationResult<Annotation>(hasErrors,
					response.getStatusLine().getStatusCode(), response
							.getStatusLine().getReasonPhrase(), annotation));
		} else {
			super.notifyListeners(new OperationResult<Annotation>(hasErrors, 0,
					"Problems with the network", null));
		}
	}
}
