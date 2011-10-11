package nu.placebo.whatsuptest.utiltest;

import com.google.android.maps.GeoPoint;
import android.os.Bundle;
import android.test.*;
import android.util.Log;
import nu.placebo.whatsup.util.GeoPointUtil;

public class GeoPointUtilTest extends AndroidTestCase {

	public GeoPointUtilTest() {
		super();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testConvertAreaToDoubles() {
		double[] d = {1.0, 2.0, 3.0, 4.0};
		double[] e = GeoPointUtil.convertAreaToDoubles(
				new GeoPoint(1000000, 2000000), new GeoPoint(3000000, 4000000));
		assertEquals(d[0], e[0]);
		assertEquals(d[1], e[1]);
		assertEquals(d[2], e[2]);
		assertEquals(d[3], e[3]);
	}
	
	public void testConvertGeoPointToDoubles() {
		double[] a = {1.0, 1.0};
		double[] b = GeoPointUtil.convertGeoPointToDoubles(new GeoPoint(1000000, 1000000));
		assertEquals(a[0], b[0]);
		assertEquals(a[1], b[1]);
	}

	public void testGetBottomLeftToTopRightPoints() {
		GeoPoint bl = new GeoPoint(0, 0);
		GeoPoint tr = new GeoPoint(10, 10);
		GeoPoint[] gp = GeoPointUtil.getBottomLeftToTopRightPoints(new GeoPoint(5, 5), 10, 10);
		assertEquals(bl.getLatitudeE6(), gp[0].getLatitudeE6());
		assertEquals(bl.getLongitudeE6(), gp[0].getLongitudeE6());
		assertEquals(tr.getLatitudeE6(), gp[1].getLatitudeE6());
		assertEquals(tr.getLongitudeE6(), gp[1].getLongitudeE6());
	}

	//public void testPushGeoPoint() {}

	//public void testPopGeoPoint() {}

	//public void testBundleHasGeoPoint() {}
}
