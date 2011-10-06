package nu.placebo.whatsup.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


/**
 * Clean map for picking positions
 * 
 * 
 * 
 * @author max
 *
 */
public class PositionPickerActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstance){
		/*
		 * Validate the incoming bundle, and find out whether the next activity should be CreateAnnotationActivity
		 * or CreateReferenceActivity
		 * 
		 * If bundle is empty or doesn't match either, kill self.
		 * 
		 * if next activity is CreateAnnotation, check with SessionHandler if logged in. If not, start log in activity.
		 */
	}
	
	public void onClick(View v){
		/*
		 * On "done" (or equal)
		 * 
		 * Start CreateAnnotation or CreateReference-activity and provide the GeoPosition picked in the intent.
		 * 
		 */
	}
}
