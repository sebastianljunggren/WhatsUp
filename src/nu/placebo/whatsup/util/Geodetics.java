package nu.placebo.whatsup.util;

import nu.placebo.whatsup.R;
import android.content.res.Resources;

import com.google.android.maps.GeoPoint;
public class Geodetics {
	
	/**
	 * Haversine formula for calculating distance between two points denoted with microdegree lat/long coordinates.
	 * This method takes the shortest way over a great circle, which makes it more accurate on longer distances on any bearing.
	 * 
	 * 
	 * @param a - GeoPoint a
	 * @param b - GeoPoint b
	 * @return distance in meters
	 */
	public static double distanceFine(GeoPoint a, GeoPoint b){
		
		double deltaLat = Math.toRadians((double)(b.getLatitudeE6() - a.getLatitudeE6()) / 1000000);
		double deltaLong = Math.toRadians((double)(b.getLongitudeE6() - a.getLongitudeE6()) / 1000000);
		double aLat = Math.toRadians((double)a.getLatitudeE6()/1000000);
		double bLat = Math.toRadians((double)b.getLatitudeE6()/1000000);
		
		double alpha = Math.sin(deltaLat /2) * Math.sin(deltaLong / 2) + Math.cos(aLat)* Math.cos(bLat) * 
				Math.sin(deltaLong / 2) * Math.sin(deltaLong);
		
		return 2 * Math.atan2(Math.sqrt(alpha), Math.sqrt(1 - alpha)) * 6371;
	}
	
	/**
	 * Pythagoras theorem with a  mercator projection. Efficient and accurate enough on short distances.
	 * 
	 * @param a - GeoPoint
	 * @param b - GeoPoint
	 * @return distance in meters
	 */
	public static double distanceCoarse(GeoPoint a, GeoPoint b){
		double aLat = Math.toRadians(((double)a.getLatitudeE6())/1000000);
		double bLat = Math.toRadians(((double)b.getLatitudeE6())/1000000);
		double aLong = Math.toRadians(((double)a.getLongitudeE6())/100000);
		double bLong = Math.toRadians(((double)b.getLongitudeE6())/100000);
		
		
		double x =  (bLong-aLong) * Math.cos((aLat+bLat)/2);
		double y = (bLat - aLat);
		
		return Math.sqrt(x*x + y*y)*6371000;
	}
	
	/**
	 * Produces a formatted string with a convenient unit depending on the actual distance.
	 * 
	 * Distances between 1 and 1000 m are presented: "123 m" (ex. dist = 123.4567)
	 * Distances between 1000 and 100 000 m are presented "12.3 km" (ex. dist = 12345.67)
	 * Distances exceeding 100 km will result in a "far away" string (ex. dist = 123456.7) 
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
			distance = Math.round((distance/100))/10;
			result = distance+" km";
		} else {
			result = "Very far away";
		}
		
		
		return result;
	}
	
	public static String distanceWithUnit(GeoPoint a, GeoPoint b){
		return distanceWithUnit(distanceCoarse(a,b));
	}
}
