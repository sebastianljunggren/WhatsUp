package nu.placebo.whatsuptest.utiltest;

import nu.placebo.whatsup.util.Geodetics;
import android.test.InstrumentationTestCase;

public class GeodeticsTest extends InstrumentationTestCase {
	
	public GeodeticsTest(){
		
	}
	
	public void setUp(){
		
	}
	
	public void distancePresentationTest(){
		double hundredmeters = 100.4;
		
		String result = nu.placebo.whatsup.util.Geodetics.distanceWithUnit(hundredmeters);
		
		assertEquals("100.4 returns 100 m", "100 m", result);
		
		
	}
}
