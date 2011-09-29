package nu.placebo.whatsup;

import java.util.List;

import nu.placebo.whatsup.model.Marker;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapViewActivity extends MapActivity {

	private MapView mapView;
	private Marker marker;
	private List<Overlay> overlays;
	private GeoPoint geoPoint;
	private OverlayItem overlayItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		overlays = mapView.getOverlays();
		
		addMarker();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void addMarker(){
		geoPoint = new GeoPoint(57716666, 11983333);
		overlayItem = new OverlayItem(geoPoint, "Alfa", "Beta");
		marker = new Marker(this.getResources().getDrawable(R.drawable.pin));
		marker.addOverlay(overlayItem);		
		overlays.add(marker);
	}
	
}
