package nu.placebo.whatsup;

import java.util.List;

import nu.placebo.whatsup.model.ExtendedOverlayItem;
import nu.placebo.whatsup.model.Marker;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapViewActivity extends MapActivity {

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
		marker = new Marker(this.getResources().getDrawable(R.drawable.pin3));
		addMarker(new GeoPoint(0, 0), "TEST1");
		addMarker(new GeoPoint(0, 10000000), "TEST2");
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void addMarker(GeoPoint point, String title) {
		marker.addOverlay(new ExtendedOverlayItem(point, title, 0));	//TODO add a real id	
		overlays.add(marker);
	}
	
}
