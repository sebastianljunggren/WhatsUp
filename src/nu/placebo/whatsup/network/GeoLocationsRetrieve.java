package nu.placebo.whatsup.network;

import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.model.GeoLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoLocationsRetrieve extends
		AbstractNetworkOperation<List<GeoLocation>> {

	private double latitudeA;
	private double longitudeA;
	private double latitudeB;
	private double longitudeB;

	public GeoLocationsRetrieve(double latitudeA, double longitudeA,
			double latitudeB, double longitudeB) {
		this.latitudeA = latitudeA;
		this.longitudeA = longitudeA;
		this.latitudeB = latitudeB;
		this.longitudeB = longitudeB;
	}
	
	public GeoLocationsRetrieve(double[] area){
		this.latitudeA = area[0];
		this.longitudeA = area[1];
		this.latitudeB = area[2];
		this.longitudeB = area[3];
	}

	public void execute() {
		String result = NetworkCalls.retrieveAnnotationMarkers(latitudeA,
				longitudeA, latitudeB, longitudeB);
		List<GeoLocation> geoLocations = new ArrayList<GeoLocation>();
		JSONArray json = null;
		try {
			json = new JSONArray(result);
			for (int i = 0; i < json.length(); i++) {
				JSONObject j = json.getJSONObject(i);
				geoLocations.add(new GeoLocation(j));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		super.notifyListeners(geoLocations);
	}
}
