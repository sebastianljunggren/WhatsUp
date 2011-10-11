package nu.placebo.whatsup.model;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Data carrier with all information an annotation consists of.
 * 
 */

public class Annotation {
	private GeoLocation geoLocation;
	private String body;
	private String author;

	private List<Comment> comments = new LinkedList<Comment>();

	public Annotation(GeoLocation geoLocation, String body,

	String author, List<Comment> comments) {
		this.geoLocation = new GeoLocation(geoLocation);
		this.body = body;
		this.author = author;
		this.comments = comments;

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

	public int getId() {
		return geoLocation.getId();
	}

	public List<Comment> getComments() {
		return comments;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof Annotation) {
			Annotation other = (Annotation) o;
			return this.getId() == other.getId() && 
				   this.getAuthor().equals(other.getAuthor()) &&
				   this.getBody().equals(other.getBody()) && 
				   this.getGeoLocation().equals(other.getGeoLocation()) &&
				   this.getComments().equals(other.getComments());
		} else {
			return false;
		}
	}
}
