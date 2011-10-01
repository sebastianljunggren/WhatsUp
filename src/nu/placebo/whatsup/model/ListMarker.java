package nu.placebo.whatsup.model;

public class ListMarker {
	private GeoLocation location;
	private double range;
	
	public ListMarker(GeoLocation location, GeoLocation reference){
		this.location = new GeoLocation(location);
		
	}
	
	public String getTitle(){
		return this.location.getTitle();
	}
	
	public String getRange(){
		

		return "range yet to be determined";
	}
	
	public String getRating(){
		return "unknown";
	}
	
	public int getId(){
		return location.getId();
	}
}
