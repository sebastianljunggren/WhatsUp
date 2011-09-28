package nu.placebo.whatsup.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Annotation {
	private GeoLocation geolocation;

	public Annotation(String json) throws JSONException {
		this(new JSONObject(json));
	}

	public Annotation(JSONObject json) throws JSONException {
		this.geolocation = new GeoLocation(json.getInt("nid"),
				json.getDouble("hej"), json.getDouble("hej"), "hej");
	}
	// TODO Define what an annotation consists of more than a GeoLocation.

	public GeoLocation getGeolocation() {
		return geolocation;
	}
}
