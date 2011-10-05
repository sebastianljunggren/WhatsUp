package nu.placebo.whatsup.util;

import com.google.android.maps.GeoPoint;
import java.math.*;
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
	 * Pythagoras theorem with a transverse mercator projection. Efficient and accurate enough on short distances.
	 * 
	 * @param a - GeoPoint
	 * @param b - GeoPoint
	 * @return distance in meters
	 */
	public static double distanceCoarse(GeoPoint a, GeoPoint b){
		double aLat = Math.toRadians((double)a.getLatitudeE6()/1000000);
		double bLat = Math.toRadians((double)b.getLatitudeE6()/1000000);
		double aLong = Math.toRadians((double)a.getLongitudeE6()/100000);
		double bLong = Math.toRadians((double)b.getLongitudeE6()/100000);
		
		double x =  (bLong-aLong) * Math.cos((aLat+bLat)/2);
		double y = (bLat - aLat);
		
		return Math.sqrt(x*x + y*y);
	}
	
	public static String distanceWithUnit(double dist){
		String result = "";
		if(dist<0)
			dist = -dist;
		
		
		if(dist < 1000){
			dist = Math.round(dist);
			result = dist+" m";
		}
		
		
		return result;
	}
}
