package nu.placebo.whatsup.util;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;

public class GeoPointUtil {

	/**
	 * Converts two GeoPoints and delivers an array with decimal degree doubles
	 * in the order:
	 * 
	 * minLat, minLong, maxLat, maxLong
	 * 
	 * The coordinates will be ordered in this manner regardless of which
	 * coordinate belongs to which GeoPoint
	 * 
	 * @param a
	 * @param b
	 * @return [minLat, minLong, maxLat, maxLong]
	 */
	public static double[] convertAreaToDoubles(GeoPoint a, GeoPoint b) {
		double[] result = new double[4];

		result[0] = ((double) Math.min(a.getLatitudeE6(), b.getLatitudeE6())) / 1000000;
		result[1] = ((double) Math.min(a.getLongitudeE6(), b.getLongitudeE6())) / 1000000;
		result[2] = ((double) Math.max(a.getLatitudeE6(), b.getLatitudeE6())) / 1000000;
		result[3] = ((double) Math.max(a.getLongitudeE6(), b.getLongitudeE6())) / 1000000;

		return result;
	}

	/**
	 * Converts a single GeoPoint to an array of decimal degree doubles
	 * 
	 * 
	 * @param a
	 * @return [lat, long]
	 */
	public static double[] convertGeoPointToDoubles(GeoPoint a) {
		double[] result = new double[2];

		result[0] = ((double) a.getLatitudeE6()) / 1000000;
		result[1] = ((double) a.getLongitudeE6()) / 1000000;

		return result;
	}

	/**
	 * Creates two GeoPoints with the center of the map, latitude span and longitude span
	 * 
	 * @param center - center of the current mapview
	 * @param latSpan - latitude span from bottom to top
	 * @param longSpan - longitude span from left to right
	 * @return GeoPoint[] with two GeoPoints representing the bottom-left and top-right coordinates 
	 */
	public static GeoPoint[] getBottomLeftToTopRightPoints(GeoPoint center,	int latSpan, int longSpan) {
		int bottom = center.getLatitudeE6() - (latSpan / 2);
		int top = center.getLatitudeE6() + (latSpan / 2);
		int left = center.getLongitudeE6() - (longSpan / 2);
		int right = center.getLongitudeE6() + (longSpan / 2);
		return new GeoPoint[] { new GeoPoint(bottom, left), new GeoPoint(top, right) };
	}

	/**
	 * Convenience method for packing a GeoPoint in a Bundle (android.os)
	 * 
	 * 
	 * 
	 * @see #popGeoPoint(Bundle)
	 * @see #bundleHasGeoPoint(Bundle)
	 * @param p
	 * @return Bundle with packaged GeoPoint
	 */
	public static Bundle pushGeoPoint(GeoPoint p) {
		Bundle bundle = new Bundle();
		bundle.putInt("lat", p.getLatitudeE6());
		bundle.putInt("long", p.getLongitudeE6());

		return bundle;
	}

	/**
	 * Convenience method for unpacking a GeoPoint from a android.os.Bundle bundle must be
	 * packed according to local standards.
	 * 
	 * @param bundle
	 * @return GeoPoint
	 */
	public static GeoPoint popGeoPoint(Bundle bundle) {
		GeoPoint p = new GeoPoint(bundle.getInt("lat"), bundle.getInt("long"));

		return p;
	}

	/**
	 * Returns whether the bundle contains a properly packaged GeoPoint
	 * according to local standards.
	 * 
	 * @param b
	 * @return
	 */
	public static boolean bundleHasGeoPoint(Bundle b) {
		return b.containsKey("lat") && b.containsKey("long");
	}
}
