package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.MenuHandler;
import nu.placebo.whatsup.model.SessionHandler;
import nu.placebo.whatsup.model.SessionInfo;
import nu.placebo.whatsup.service.model.DataProvider;
import nu.placebo.whatsup.util.GeoPointUtil;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

/**
 * Clean map for picking positions.
 * 
 * @author Max Witt
 */

public class PositionPickerActivity extends MapActivity implements
		OnClickListener {

	private MapView mapView;
	private int requestCode;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.position_picker_view);

		this.requestCode = this.getIntent().getExtras().getInt("requestCode");
		TextView typeText = (TextView) this.findViewById(R.id.activity_name);
		EditText refName = (EditText) this.findViewById(R.id.position_name);

		if (this.requestCode == Constants.ANNOTATION) {
			refName.setVisibility(View.GONE);
			typeText.setText("New Annotation");

		} else if (this.requestCode == Constants.REFERENCE_POINT) {

			refName.setVisibility(View.VISIBLE);
			typeText.setText("New Reference Point");

		} else {
			this.finish();
		}

		mapView = (MapView) findViewById(R.id.position_picker_mapview);
		mapView.setBuiltInZoomControls(true);
		Button selectPosition = (Button) findViewById(R.id.select_position);
		selectPosition.setOnClickListener(this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onResume() {
		if (this.requestCode == Constants.ANNOTATION && !SessionHandler.getInstance(this).hasSession()) {
			Intent intent = new Intent(this, LogInActivity.class);
			this.startActivityForResult(intent, Constants.LOG_IN);
		}
		super.onResume();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.select_position) {
			GeoPoint p = mapView.getMapCenter();
			if (this.requestCode == Constants.ANNOTATION) {
				Intent intent = new Intent(this, CreateAnnotationActivity.class);
				intent.putExtras(GeoPointUtil.pushGeoPoint(p));
				this.startActivityForResult(intent, Constants.ANNOTATION);
			}

			if (this.requestCode == Constants.REFERENCE_POINT) {
				String refName = ((EditText) this
						.findViewById(R.id.position_name)).getText().toString();
				if (!refName.equals("")) {
					DataProvider.getDataProvider(this).addReferencePoint(p,
							refName);
					this.finish();
				}
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
		Log.d("whatsup", "Returned to PositionPickerActivity with resultCode: "
				+ resultCode);
		if (requestCode == Constants.ANNOTATION) {
			if (resultCode == Constants.ACTIVITY_FINISHED_OK) {
				Log.d("whatsup", "CreateAnnotationActivity finished OK");
				this.finish();
			}
			if (resultCode == Constants.ACTIVITY_INTERRUPTED) {
				Log.d("whatsup", "CreateAnnotationActivity did interrupt");
			}
		}

		if (requestCode == Constants.LOG_IN) {
			if (resultCode == Constants.ACTIVITY_INTERRUPTED) {
				this.finish();
			}
			if (resultCode == Constants.ACTIVITY_FINISHED_OK) {
				this.onResume();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuHandler.inflate(menu, this.getMenuInflater());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuHandler.onOptionsItemSelected(item, this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return MenuHandler.onPrepareOptionsMenu(menu, this);
	}
}
