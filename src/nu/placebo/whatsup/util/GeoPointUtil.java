package nu.placebo.whatsup.util;

import com.google.android.maps.GeoPoint;

public class GeoPointUtil {
	
	
	/**
	 * Converts two GeoPoints and delivers an array with decimal degrees doubles in the order:
	 * 
	 * minLat, minLong, maxLat, maxLong
	 * 
	 * The coordinates will be ordered in this manner regardless of which coordinate belongs to which GeoPoint
	 * 
	 * @param a
	 * @param b
	 * @return [minLat, minLong, maxLat, maxLong]
	 */
	public static double[] convertAreaToDoubles(GeoPoint a, GeoPoint b){
		double[] result = new double[4];
		
		result[0] = Math.min(a.getLatitudeE6(), b.getLatitudeE6()) /1000000;
		result[1] = Math.min(a.getLongitudeE6(), b.getLongitudeE6()) /1000000;
		result[2] = Math.max(a.getLatitudeE6(), b.getLatitudeE6()) /1000000;
		result[3] = Math.max(a.getLongitudeE6(), b.getLongitudeE6()) /1000000;
		
		return result;
	}
	
	
}
