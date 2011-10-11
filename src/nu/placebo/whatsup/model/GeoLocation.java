package nu.placebo.whatsup.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class GeoLocation {
	private GeoPoint gp;
	private String title;
	private int id;

	public GeoLocation(int id, int microLat, int microLong, String title) {
		this.gp = new GeoPoint(microLat, microLong);
		this.title = title;
		this.id = id;
	}

	public GeoLocation(GeoLocation gl) {
		this.gp = gl.gp;
		this.title = gl.title;
		this.id = gl.id;
	}

	public GeoLocation(int id, double latitude, double longitude, String title) {
		this(id, (int) (latitude * 1000000 + 0.5),
				(int) (longitude * 1000000 + 0.5), title);

	}

	public GeoLocation(JSONObject j) throws JSONException {
		this(j.getInt("nid"), j.getDouble("latitude"),
				j.getDouble("longitude"), j.getString("title"));
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the location
	 */
	public GeoPoint getLocation() {
		return gp;
	}

	/**
	 * @param gp
	 *            the gp to set
	 */
	public void setLocation(GeoPoint gp) {
		this.gp = gp;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof GeoLocation) {
			GeoLocation other = (GeoLocation) o;
			return this.getId() == other.getId() &&
				   this.getTitle().equals(other.getTitle()) &&
				   this.getLocation().equals(other.getLocation());
		}
		return false;
	}
}
