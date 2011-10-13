package nu.placebo.whatsuptest.utiltest;

import com.google.android.maps.GeoPoint;

import nu.placebo.whatsup.util.Geodetics;
import android.test.AndroidTestCase;

public class GeodeticsTest extends AndroidTestCase {

	public GeodeticsTest() {
		super();
	}

	public void setUp() throws Exception {

		super.setUp();
	}

	public void testPresentMeterPrecision() {
		double hundredmeters = 100.4;

		String result = Geodetics.distanceWithUnit(hundredmeters);
		assertEquals("100.4 returns 100 m", "100 m", result);

	}

	public void testPresentKMeterPrecision() {
		double nearlyKm = -999.7;
		/*
		 * should auto-negate the value to show a positive distance.
		 */

		String result = Geodetics.distanceWithUnit(nearlyKm);

		assertEquals("999.7 returns 1000 m", "1.0 km", result);
	}

	public void testPresentFarLimit() {
		/*
		 * The far limit is 1000 km exclusive, which may present 999.9 km.
		 * 1000.0 km should thus never be displayed.
		 */

		double nearlyFar = 999970.0;

		String result = Geodetics.distanceWithUnit(nearlyFar);

		assertEquals("999.970 m returns far away", "Very far away", result);
	}

	public void testDistanceOne() {
		GeoPoint a = new GeoPoint(55000000, 12000000);
		GeoPoint b = new GeoPoint(55000009, 12000000);

		double distance = Math.round(Geodetics.distance(a, b));

		assertEquals("Distance should be 1 meter", 1.0, distance);
	}

	public void testDistanceEquator() {
		/*
		 * Testing one degree at equator
		 */

		GeoPoint a = new GeoPoint(0, 12000000);
		GeoPoint b = new GeoPoint(0, 13000000);

		double distance = Math.round(Geodetics.distance(a, b) / 100);
		/*
		 * Tolerate within 100 m
		 */

		assertEquals("Equator:", 1112.0, distance);

	}

	public void testDistanceFarNorth() {
		/*
		 * Testing one degree very far north
		 */
		GeoPoint a = new GeoPoint(80000000, 12000000);
		GeoPoint b = new GeoPoint(80000000, 12020000);

		double distance = Math.round(Geodetics.distance(a, b));
		/*
		 * Tolerate within 100 m
		 */
		
		assertEquals("Far north", 386.0, distance);
	}
	
	public void testTwoGeoPoints(){
		
		GeoPoint brunnsparken = new GeoPoint(57706704,11968081);
		GeoPoint nordstan = new GeoPoint(57709240, 11970747);
		
		String distance1 = Geodetics.distanceWithUnit(brunnsparken, nordstan);
		
		assertEquals("Less to more", "323 m", distance1);
		
		String distance2 = Geodetics.distanceWithUnit(nordstan, brunnsparken);
		
		assertEquals("More to less", distance1, distance2);
	}
}
