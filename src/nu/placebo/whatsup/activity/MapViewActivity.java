package nu.placebo.whatsup.activity;

import java.util.List;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.ExtendedOverlayItem;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.Marker;
import nu.placebo.whatsup.model.MenuHandler;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkTask;
import nu.placebo.whatsup.network.OperationResult;
import nu.placebo.whatsup.util.GeoPointUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Activity that holds the map and control map actions. It's the "main"
 * activity.
 * 
 * @author Albin Bramstång
 */
public class MapViewActivity extends MapActivity implements OnClickListener,
		NetworkOperationListener<List<GeoLocation>> {

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
		marker = new Marker(this.getResources().getDrawable(R.drawable.pin),
				mapView, this);
		setupToolbar();
	}

	private void setupToolbar() {
		ImageButton gotoListBtn = (ImageButton) this
				.findViewById(R.id.map_goto_list);
		gotoListBtn.setOnClickListener(this);
		ImageButton refreshBtn = (ImageButton) this
				.findViewById(R.id.map_refresh);
		refreshBtn.setOnClickListener(this);
		ImageButton addAnnotationButton = (ImageButton) findViewById(R.id.add_annotation);
		addAnnotationButton.setOnClickListener(this);
		ImageButton refPointBtn = (ImageButton) this.findViewById(R.id.goto_refpoint);
		refPointBtn.setOnClickListener(this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Creates overlays and adds them to the map.
	 * 
	 * @param g
	 */
	private void addMarkers(List<GeoLocation> g) {
		for (GeoLocation h : g) {
			marker.addOverlay(new ExtendedOverlayItem(h));
		}
		marker.callPopulate();
		overlays.add(marker);
		mapView.invalidate();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.map_goto_list) {
			Intent intent = new Intent(MapViewActivity.this,
					ListViewActivity.class);
			this.startActivity(intent);
			this.finish();
	/*	} else if (v.getId() == R.id.log_in) {
			Intent intent = new Intent(MapViewActivity.this,
					LogInActivity.class);
			this.startActivity(intent); */
		} else if (v.getId() == R.id.map_refresh) {
			refresh();
		} else if (v.getId() == R.id.add_annotation) {
			Intent intent = new Intent(MapViewActivity.this,
					PositionPickerActivity.class);
			intent.putExtra("requestCode", Constants.ANNOTATION);
			this.startActivity(intent);
		}
		if(v.getId() == R.id.goto_refpoint){
			Intent intent = new Intent(this, RefPointActivity.class);
			this.startActivity(intent);
		}
	}

	/**
	 * Retrieves information about the current location on the map, and sends it
	 * to the server.
	 */
	@SuppressWarnings("unchecked")
	public void refresh() {
		marker.clear();
		GeoPoint[] p = GeoPointUtil.getBottomLeftToTopRightPoints(
				mapView.getMapCenter(), mapView.getLatitudeSpan(),
				mapView.getLongitudeSpan());
		double[] d = GeoPointUtil.convertAreaToDoubles(p[0], p[1]);
		GeoLocationsRetrieve gr = new GeoLocationsRetrieve(d[0], d[1], d[2], d[3]);
		gr.addOperationListener(this);
		new NetworkTask<List<GeoLocation>>().execute(gr);
	}

	public void operationExcecuted(
			final OperationResult<List<GeoLocation>> result) {
		if (!result.hasErrors()) {
					addMarkers(result.getResult());
		}
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
