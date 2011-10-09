package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.GeoLocation;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Retrieves GeoLocations within the specified range from the server.
 */

public class GeoLocationsRetrieve extends
		AbstractNetworkOperation<List<GeoLocation>> {

	private double latitudeA;
	private double longitudeA;
	private double latitudeB;
	private double longitudeB;
	private boolean hasErrors;

	public GeoLocationsRetrieve(double latitudeA, double longitudeA,
			double latitudeB, double longitudeB) {
		this.latitudeA = latitudeA;
		this.longitudeA = longitudeA;
		this.latitudeB = latitudeB;
		this.longitudeB = longitudeB;
	}

	public GeoLocationsRetrieve(double[] area) {
		this.latitudeA = area[0];
		this.longitudeA = area[1];
		this.latitudeB = area[2];
		this.longitudeB = area[3];
	}

	public void execute() {
		this.hasErrors = true;
		HttpResponse response = NetworkCalls
				.performGetRequest(Constants.API_URL + "nearby/"
						+ this.latitudeA + "," + this.longitudeA + ","
						+ this.latitudeB + "," + this.longitudeB + ".json");
		ResponseHandler<String> handler = new BasicResponseHandler();
		String result = null;
		try {
			result = handler.handleResponse(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<GeoLocation> geoLocations = this.parse(result);

		if (response != null) {
			super.notifyListeners(new OperationResult<List<GeoLocation>>(
					this.hasErrors, response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase(), geoLocations));
		} else {
			super.notifyListeners(new OperationResult<List<GeoLocation>>(
					hasErrors, 0, "Problems with the network", null));
		}

	}

	private List<GeoLocation> parse(String result) {
		List<GeoLocation> geoLocations = new ArrayList<GeoLocation>();
		if (result != null) {
			try {
				JSONArray json = new JSONArray(result);
				for (int i = 0; i < json.length(); i++) {
					JSONObject j = json.getJSONObject(i);
					geoLocations.add(new GeoLocation(j));
				}
				this.hasErrors = false;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return geoLocations;
	}
}
