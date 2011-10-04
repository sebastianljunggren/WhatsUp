package nu.placebo.whatsup.activity;

import java.util.List;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.R.drawable;
import nu.placebo.whatsup.R.id;
import nu.placebo.whatsup.R.layout;
import nu.placebo.whatsup.model.ExtendedOverlayItem;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.Marker;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Activity that holds the map and control map actions. It's the "main" activity.
 * 
 * 
 * @author Ablim
 */
public class MapViewActivity extends MapActivity implements OnClickListener {

	private MapView mapView;
	private Marker marker;
	private List<Overlay> overlays;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		overlays = mapView.getOverlays();
		marker = new Marker(this.getResources().getDrawable(R.drawable.pin3), mapView, this);
		addMarker(new GeoLocation(1234, 57716666, 11983333, "Göteborg"));
		
		setupToolbar();
		
		
	}

	private void setupToolbar() {
		
		Button annotationBtn = (Button) this.findViewById(R.id.showAnnotation);
		annotationBtn.setOnClickListener(this);
		Button gotoListBtn = (Button) this.findViewById(R.id.map_goto_list);
		gotoListBtn.setOnClickListener(this);
		Button logInBtn = (Button) this.findViewById(R.id.log_in);
		logInBtn.setOnClickListener(this);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void addMarker(GeoLocation g) {
		marker.addOverlay(new ExtendedOverlayItem(g));
		overlays.add(marker);
	}

	public void onClick(View v) {
		
		if(v.getId() == R.id.showAnnotation){
			Intent intent = new Intent(MapViewActivity.this, AnnotationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("nid", 1234);
			intent.putExtras(bundle);
			this.startActivity(intent);
		} else if(v.getId() == R.id.map_goto_list){
			Intent intent = new Intent(MapViewActivity.this, ListViewActivity.class);
			this.startActivity(intent);
			this.finish();
		} else if (v.getId() == R.id.log_in) {
			Intent intent = new Intent(MapViewActivity.this, LogInActivity.class);
			this.startActivity(intent);
			
		}

	}
	
}
