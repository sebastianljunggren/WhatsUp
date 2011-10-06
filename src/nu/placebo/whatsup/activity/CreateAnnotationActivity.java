package nu.placebo.whatsup.activity;

import com.google.android.maps.GeoPoint;

import nu.placebo.whatsup.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CreateAnnotationActivity extends Activity implements OnClickListener{

	private GeoPoint gp;
	private TextView titleField;
	private TextView descField;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle gPBundle = this.getIntent().getExtras();
		
		// if bundle does not contain a GeoPoint
		// kill self.
		// else 
		// gp = gPBundle.getGeoPoint();
		
		/* if SessionHandler has no session
		 * kill self
		 * 
		 */
		
		this.setContentView(R.layout.create_annotation);
		this.titleField = (TextView) this.findViewById(R.id.create_annot_title);
		this.descField = (TextView) this.findViewById(R.id.create_annot_desc);
		
		Button submitBtn = (Button) this.findViewById(R.id.create_annot_submit);
		submitBtn.setOnClickListener(this);
		
	}
	
	public void onClick(View v){
		
		/*
		 * Retrieve all data from textfields and such,
		 * parse and validate;
		 * create and send Annotation object to service/network caller.
		 */
		
	}
	

}
