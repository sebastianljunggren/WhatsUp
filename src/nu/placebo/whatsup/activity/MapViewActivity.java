package nu.placebo.whatsup.activity;

import java.util.List;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.ExtendedOverlayItem;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.Marker;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.Login;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;
import nu.placebo.whatsup.util.GeoPointUtil;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Activity that holds the map and control map actions. It's the "main" activity.
 * 
 * @author Ablim
 */
public class MapViewActivity extends MapActivity implements OnClickListener, NetworkOperationListener<List<GeoLocation>> {

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
		
		setupToolbar();
	}
	
	private void setupToolbar() {
		Button gotoListBtn = (Button) this.findViewById(R.id.map_goto_list);
		gotoListBtn.setOnClickListener(this);
		Button logInBtn = (Button) this.findViewById(R.id.log_in);
		logInBtn.setOnClickListener(this);
		Button refreshBtn = (Button) this.findViewById(R.id.map_refresh);
		refreshBtn.setOnClickListener(this);
			 
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void addMarkers(List<GeoLocation> g) {
		for (GeoLocation h : g) {
			marker.addOverlay(new ExtendedOverlayItem(h));
		}
		marker.callPopulate();
		overlays.add(marker);
		mapView.invalidate();
	}

	public void onClick(View v) {
		if(v.getId() == R.id.map_goto_list){
			Intent intent = new Intent(MapViewActivity.this, ListViewActivity.class);
			this.startActivity(intent);
			this.finish();
		} else if (v.getId() == R.id.log_in) {
			Intent intent = new Intent(MapViewActivity.this, LogInActivity.class);
			this.startActivity(intent);
		} else if (v.getId() == R.id.map_refresh) {
			refresh();
		}
	}
	
	public void refresh() {
		marker.clear();
		GeoPoint[] p = GeoPointUtil.getBottomLeftToTopRightPoints(mapView.getMapCenter(), mapView.getLatitudeSpan(), 
				mapView.getLongitudeSpan());
		double[] d = GeoPointUtil.convertAreaToDoubles(p[0], p[1]);
		GeoLocationsRetrieve gr = new GeoLocationsRetrieve(d[0], d[1], d[2], d[3]);
		gr.addOperationListener(this);
		NetworkQueue.getInstance().add(gr);
	}

	public void operationExcecuted(final List<GeoLocation> result) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				addMarkers(result);
			}	
		});
	}
	
}
