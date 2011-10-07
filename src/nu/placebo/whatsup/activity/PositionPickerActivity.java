package nu.placebo.whatsup.activity;

import android.app.Activity;
import android.content.Intent;
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

	private boolean isCreateAnnotation;

	@Override
	public void onCreate(Bundle savedInstance) {
		/*
		 * Validate the incoming bundle, and find out whether the next activity
		 * should be CreateAnnotationActivity or CreateReferenceActivity
		 * 
		 * If bundle is empty or doesn't match either, kill self.
		 * 
		 * if next activity is CreateAnnotation, check with SessionHandler if
		 * logged in. If not, start log in activity.
		 */
	}

	public void onClick(View v) {
		if (this.isCreateAnnotation) {
			Intent intent = new Intent(this, CreateAnnotationActivity.class);
			this.startActivityForResult(intent, 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// Activity should kill self if result code means that the child
		// submitted its data and everything is OK.
		super.onActivityResult(requestCode, resultCode, data);
	}

}
