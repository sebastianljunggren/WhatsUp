package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.SessionInfo;
import nu.placebo.whatsup.util.GeoPointUtil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

/**
 * 
 * Retrieves an Annotation from the server.
 * 
 */

public class AnnotationCreate extends AbstractNetworkOperation<Annotation> {

	private String title, body, author;
	private SessionInfo sessionInfo;
	private double latitude;
	private double longitude;
	private boolean hasErrors;

	/**
	 * 
	 * Sends an request to create an annotation to the server. Requires a valid
	 * SessionInfo.
	 * 
	 */
	public AnnotationCreate(String title, String body, String author,
			GeoPoint pos, SessionInfo sessionInfo) {
		this.title = title;
		this.body = body;
		double[] p = GeoPointUtil.convertGeoPointToDoubles(pos);
		this.latitude = p[0];
		this.longitude = p[1];
		this.sessionInfo = sessionInfo;
		this.author = author;
	}

	public void execute() {
		Annotation annotation = null;
		HttpResponse response = null;
		this.hasErrors = true;
		try {
			List<NameValuePair> body = new ArrayList<NameValuePair>(2);
			body.add(new BasicNameValuePair("node[title]", this.title));
			body.add(new BasicNameValuePair("node[type]", "annotation"));
			body.add(new BasicNameValuePair("node[body][und][0][value]",
					this.body));
			body.add(new BasicNameValuePair("node[locations][0][country]", "se"));
			body.add(new BasicNameValuePair(
					"node[locations][0][locpick][user_latitude]", this.latitude
							+ ""));
			body.add(new BasicNameValuePair(
					"node[locations][0][locpick][user_longitude]",
					this.longitude + ""));
			response = NetworkCalls.performPostRequest(Constants.API_URL
					+ "node.json", body, this.sessionInfo);
			ResponseHandler<String> handler = new BasicResponseHandler();
			annotation = this.parseResult(handler.handleResponse(response));
			hasErrors = false;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.notifyListeners(new OperationResult<Annotation>(hasErrors,
				response.getStatusLine().getStatusCode(), response
						.getStatusLine().getReasonPhrase(), annotation));
	}

	private Annotation parseResult(String result) {
		try {
			JSONObject json = new JSONObject(result);
			int nid = json.getInt("nid");
			this.hasErrors = false;
			return new Annotation(new GeoLocation(nid,
					this.latitude, this.longitude, this.title), this.body,
					this.author, null);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}