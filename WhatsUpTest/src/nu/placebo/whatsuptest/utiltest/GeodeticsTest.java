package nu.placebo.whatsuptest.utiltest;

import junit.framework.Assert;
import junit.framework.TestCase;
import nu.placebo.whatsup.util.Geodetics;
import android.test.AndroidTestCase;

public class GeodeticsTest extends AndroidTestCase {
	
	public GeodeticsTest(){
		super();
	}
	
	public void setUp() throws Exception{
		
		super.setUp();
	}
	
	
	
	public void testDistanceMeterPrecision(){
		double hundredmeters = 100.4;
		
		String result = Geodetics.distanceWithUnit(hundredmeters);
		assertEquals("100.4 returns 100 m", "100 m", result);
		
		
		
	}
	
	public void testDistanceKMeterPrecision(){
		double nearlyKm = 999.7;
		
		String result = Geodetics.distanceWithUnit(nearlyKm);
		
		assertEquals("999.7 returns 1000 m", "1.0 km", result);
	}
	
	
	public void testDistanceFarLimit(){
		/*
		 * The far limit is 1000 km exclusive, which may present 999.9 km. 
		 * 1000.0 km should thus never be displayed.
		 */
		
		double nearlyFar = 999970.0;
		
		String result = Geodetics.distanceWithUnit(nearlyFar);
		
		assertEquals("999.970 m returns far away", "Very far away", result);
	}
}
