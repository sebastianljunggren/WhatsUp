package nu.placebo.whatsup.model;

import nu.placebo.whatsup.util.Geodetics;

public class ListMarker {
	private GeoLocation location;
	private GeoLocation ref;
	private double range;
	
	public ListMarker(GeoLocation location, GeoLocation reference){
		this.location = new GeoLocation(location);
		this.ref = new GeoLocation(reference);
	}
	
	public String getTitle(){
		return this.location.getTitle();
	}
	
	public String getRange(){
		return Geodetics.distanceWithUnit(location.getLocation(), ref.getLocation());
	}
	
	public String getRating(){
		return "unknown";
	}
	
	public int getId(){
		return location.getId();
	}
}
