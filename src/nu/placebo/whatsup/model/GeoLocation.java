package nu.placebo.whatsup.model;

import com.google.android.maps.GeoPoint;



public class GeoLocation {
	private GeoPoint gp;
	private String title;
	private int id;
	
	public GeoLocation(int id, int microLat, int microLong, String title){
		this.gp = new GeoPoint(microLat, microLong);
		this.title = title;
		this.id = id;
	}
	
	public GeoLocation(GeoLocation gl){
		this.gp = gl.gp;
		this.title = gl.title;
		this.id = gl.id;
	}
	
	public GeoLocation(int id, double latitude, double longitude, String title){
		this(id, (int) (latitude * 1000000 + 0.5), (int) (longitude * 1000000 + 0.5), title);
		
	}
	
	/**
	 * @return the ID
	 */
	public int getID() {
		return id;
	}
	
	
	/**
	 * @return the gp
	 */
	public GeoPoint getGp() {
		return gp;
	}

	/**
	 * @param gp the gp to set
	 */
	public void setGp(GeoPoint gp) {
		this.gp = gp;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}



	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
