package nu.placebo.whatsup.model;

import nu.placebo.whatsup.util.Geodetics;

public class ListMarker implements Comparable<ListMarker>{
	private GeoLocation location;
	private GeoLocation ref;
	private double range;
	
	public ListMarker(GeoLocation location, GeoLocation reference){
		this.location = new GeoLocation(location);
		this.ref = new GeoLocation(reference);
		this.range = Geodetics.distance(this.location.getLocation(), this.ref.getLocation());
	}
	
	public String getTitle(){
		return this.location.getTitle();
	}
	
	public String getRange(){
		return Geodetics.distanceWithUnit(this.range);
	}
	
	public String getRating(){
		return "";
	}
	
	public int getId(){
		return location.getId();
	}

	public boolean equals(Object o) {
		if(o instanceof ListMarker) {
			ListMarker other = (ListMarker) o;
			return this.range == other.range;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = 0;
		result += location.hashCode();
		result += ref.hashCode();
		result += range;
		return result;
	}
	
	public int compareTo(ListMarker another) {
		return (int)(this.range-another.range);
	}
}
