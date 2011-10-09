package nu.placebo.whatsup.activity;

import com.google.android.maps.GeoPoint;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.network.AnnotationCreate;
import nu.placebo.whatsup.util.GeoPointUtil;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CreateAnnotationActivity extends Activity implements
		OnClickListener {

	private GeoPoint gp;
	private TextView titleField;
	private TextView descField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(Constants.ACTIVITY_INTERRUPTED);

		Bundle bundle = this.getIntent().getExtras();
		if (GeoPointUtil.bundleHasGeoPoint(bundle)) {
			this.gp = GeoPointUtil.popGeoPoint(bundle);

		} else {
			this.finish();
		}

		this.setContentView(R.layout.create_annotation);
		this.titleField = (TextView) this.findViewById(R.id.create_annot_title);
		this.descField = (TextView) this.findViewById(R.id.create_annot_desc);
		TextView debugLat = (TextView) this
				.findViewById(R.id.create_annot_debug_lat);
		TextView debugLong = (TextView) this
				.findViewById(R.id.create_annot_debug_long);

		debugLat.setText(Integer.toString(gp.getLatitudeE6()));
		debugLong.setText(Integer.toString(gp.getLongitudeE6()));

		Button submitBtn = (Button) this.findViewById(R.id.create_annot_submit);
		submitBtn.setOnClickListener(this);

	}

	public void onClick(View v) {
		if (v.getId() == R.id.create_annot_submit) {
			setResult(Constants.ACTIVITY_FINISHED_OK);
			/*
			 * String title = (String) titleField.getText(); String desc =
			 * (String) titleField.getText(); if (title != null && title != "")
			 * {
			 * 
			 * // AnnotationCreate acOp = new AnnotationCreate("author", title,
			 * desc, gp); }
			 */
			this.finish();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	

}
