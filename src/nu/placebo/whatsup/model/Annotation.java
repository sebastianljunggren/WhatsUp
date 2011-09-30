package nu.placebo.whatsup.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Annotation {
	private GeoLocation geoLocation;
	private String body;
	private int uid;
	private String author;

	public Annotation(GeoLocation geoLocation, String body, int uid,
			String author) {
		this.geoLocation = new GeoLocation(geoLocation);
		this.body = body;
		this.author = author;
		this.uid = uid;
	}

	public Annotation(String json) throws JSONException {
		this(new JSONObject(json));
	}

	public Annotation(JSONObject json) throws JSONException {
		JSONObject location = json.getJSONObject("location");
		this.body = json.getJSONObject("body").getJSONArray("und")
				.getJSONObject(0).getString("safe_value");
		this.geoLocation = new GeoLocation(json.getInt("nid"),
				location.getDouble("latitude"),
				location.getDouble("longitude"), json.getString("title"));
		this.uid = json.getInt("uid");
		this.author = json.getString("name");
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public String getBody() {
		return body;
	}

	public int getUid() {
		return uid;
	}

	public String getAuthor() {
		return author;
	}
}
