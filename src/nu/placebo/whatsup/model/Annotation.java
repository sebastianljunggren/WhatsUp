package nu.placebo.whatsup.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Annotation {
	private GeoLocation geolocation;
	private String body;
	private int uid;
	private String author;

	public Annotation(String json) throws JSONException {
		this(new JSONObject(json));
	}

	public Annotation(JSONObject json) throws JSONException {
		JSONObject location = json.getJSONObject("location");
		this.body = json.getJSONObject("body").getJSONObject("und")
				.getString("safe_value");
		this.geolocation = new GeoLocation(json.getInt("nid"),
				location.getDouble("latitude"),
				location.getDouble("longitude"), json.getString("title"));
		this.uid = json.getInt("uid");
		this.author = json.getString("name");
	}

	public GeoLocation getGeolocation() {
		return geolocation;
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
