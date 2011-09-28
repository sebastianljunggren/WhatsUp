package nu.placebo.whatsup.model;

public class GeoLocation {
	private double latitude;
	private double longitude;
	private String title;
	private int id;
	
	public GeoLocation(int id,double latitude, double longitude, String title){
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.id = id;
	}
	
	/**
	 * @return the ID
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}


	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
