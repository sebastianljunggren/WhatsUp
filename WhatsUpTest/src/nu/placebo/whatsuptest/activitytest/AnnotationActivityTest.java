package nu.placebo.whatsuptest.activitytest;

import android.test.*;
import nu.placebo.whatsup.activity.AnnotationActivity;

public class AnnotationActivityTest extends ActivityInstrumentationTestCase2<AnnotationActivity> {

	public AnnotationActivityTest() {
		super("nu.placebo.whatsup.activity", AnnotationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testPreconditions() {
		
	}
	
	public void addTest() {
		assertEquals(true, true);
	}
}
