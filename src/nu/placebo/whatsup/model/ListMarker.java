package nu.placebo.whatsup.model;

public class ListMarker {
	private GeoLocation location;
	private double range;
	
	public ListMarker(GeoLocation location, GeoLocation reference){
		this.location = new GeoLocation(location.getLatitude(), location.getLongitude(), location.getTitle());
		this.range = Math.sqrt(Math.pow((reference.getLatitude() - location.getLatitude()), 2) + 
				Math.pow((reference.getLongitude() - location.getLongitude()), 2));
	}
	
	public String getTitle(){
		return this.location.getTitle();
	}
}
