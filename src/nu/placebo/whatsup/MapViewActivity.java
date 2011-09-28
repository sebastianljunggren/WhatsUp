package nu.placebo.whatsup;

import android.os.Bundle;
import com.google.android.maps.*;

public class MapViewActivity extends MapActivity {

	private MapView mapView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	
	private void addMarker(){
		// Get map center
		
		// Create a marker (overlay item or something)
		
		// Place it on top of the map
		
		// Be happy =)
	}
}
