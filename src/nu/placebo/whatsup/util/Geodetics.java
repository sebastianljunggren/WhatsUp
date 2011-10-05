package nu.placebo.whatsup.util;

import com.google.android.maps.GeoPoint;
public class Geodetics {
	
	
	/**
	 * Pythagoras theorem with a  mercator projection. Efficient and accurate enough on short distances.
	 * 
	 * 
	 * 
	 * @param a - GeoPoint
	 * @param b - GeoPoint
	 * @return distance in meters
	 */
	public static double distance(GeoPoint a, GeoPoint b){
		
		double[] alpha = GeoPointUtil.convertGeoPointToDoubles(a);
		double[] beta = GeoPointUtil.convertGeoPointToDoubles(b);
		
		double aLat = Math.toRadians(alpha[0]);	double bLat = Math.toRadians(beta[0]);
		double aLong = Math.toRadians(alpha[1]);	double bLong = Math.toRadians(beta[1]);		
		
		double x =  (bLong-aLong) * Math.cos((aLat+bLat)/2);
		double y = (bLat - aLat);
		
		return Math.sqrt(x*x + y*y)* 6371000;
	}
	
	/**
	 * Produces a formatted string with a convenient unit depending on the actual distance.
	 * 
	 * Distances between 1 and 1000 m are presented: "123 m" (ex. dist = 123.4567)
	 * Distances between 1000 and 100 000 m are presented "12.3 km" (ex. dist = 12345.67)
	 * Distances exceeding 1000 km will result in a "far away" string (ex. dist = 1234567) 
	 * 
	 * @param distance
	 * @return String with formatted value and approperiate unit
	 */
	public static String distanceWithUnit(double distance){
		String result = "";
		if(distance<0)
			distance = -distance;
		
		
		if(distance < 1000){
			distance = Math.round(distance);
			result = distance+" m";
		} else if(distance < 1000000){
			distance =((double) Math.round((distance/100)))/10;
			result = distance+" km";
		} else {
			result = "Very far away";
		}
		
		
		return result;
	}
	
	public static String distanceWithUnit(GeoPoint a, GeoPoint b){
		return distanceWithUnit(distance(a,b));
	}
}
