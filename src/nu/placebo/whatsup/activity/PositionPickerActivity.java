package nu.placebo.whatsup.activity;

import android.content.Intent;
import nu.placebo.whatsup.R;
import nu.placebo.whatsup.util.GeoPointUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

/**
 * Clean map for picking positions.
 * 
 * @author Max Witt
 */

public class PositionPickerActivity extends MapActivity implements OnClickListener {
	
	private MapView mapView;
	private boolean isCreateAnnotation;

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);

		/*
		 * Validate the incoming bundle, and find out whether the next activity
		 * should be CreateAnnotationActivity or CreateReferenceActivity
		 * 
		 * If bundle is empty or doesn't match either, kill self.
		 * 
		 * if next activity is CreateAnnotation, check with SessionHandler if
		 * logged in. If not, start log in activity.
		 */
		setContentView(R.layout.position_picker_view);
		mapView = (MapView) findViewById(R.id.position_picker_mapview);
		mapView.setBuiltInZoomControls(true);
		this.isCreateAnnotation = true;
		Button selectPosition = (Button) findViewById(R.id.select_position);
		selectPosition.setOnClickListener(this);
	}			
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void onClick(View v) {
		if (v.getId() == R.id.select_position) {
			if (this.isCreateAnnotation) {
				Intent intent = new Intent(this, CreateAnnotationActivity.class);
				GeoPoint p = mapView.getMapCenter();
				intent.putExtras(GeoPointUtil.pushGeoPoint(p));
				this.startActivityForResult(intent, 1);
			}
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
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == R.string.ACTIVITY_FINISH_OK){
			Log.d("whatsup", "CreateAnnotationActivity finished OK");
			
			this.finish();
		}
		if(resultCode == R.string.ACTIVITY_DID_INTERRUPT){
			Log.d("whatsup", "CreateAnnotationActivity did interrupt");
		}
		
	}

}
