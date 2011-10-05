package nu.placebo.whatsup.model;

import com.google.android.maps.GeoPoint;

public class ReferencePoint {

	private final int id;
	private GeoPoint geoPoint;

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public ReferencePoint(ReferencePoint r) {
		this(r.getId(), r.getGeoPoint(), r.getName());
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	private String name;

	public ReferencePoint(int id, GeoPoint geoPoint, String name) {
		this.id = id;
		this.geoPoint = geoPoint;
		this.name = name;
	}

}
