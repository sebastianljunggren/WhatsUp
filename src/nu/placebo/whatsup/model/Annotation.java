package nu.placebo.whatsup.model;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Annotation {
	private GeoLocation geoLocation;
	private String body;
	private String author;
	
	private List<Comment> comments = new LinkedList<Comment>();

	public Annotation(GeoLocation geoLocation, String body,
			String author) {
		this.geoLocation = new GeoLocation(geoLocation);
		this.body = body;
		this.author = author;
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
		this.author = json.getString("name");
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public String getBody() {
		return body;
	}

	public String getAuthor() {
		return author;
	}
}
