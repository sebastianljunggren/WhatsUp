package nu.placebo.whatsuptest.activitytest;

import android.test.ActivityInstrumentationTestCase2;
import nu.placebo.whatsup.activity.MapViewActivity;

public class MapViewActivityTest extends ActivityInstrumentationTestCase2<MapViewActivity> {

	private MapViewActivity mapViewActivity;
	
	public MapViewActivityTest() {
		super("nu.placebo.whatsup.activity", MapViewActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mapViewActivity = this.getActivity();
	}
	
	public void testRefresh() {
		mapViewActivity.refresh();
	}

}
