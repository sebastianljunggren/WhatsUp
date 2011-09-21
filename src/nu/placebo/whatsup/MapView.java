package nu.placebo.whatsup;

import com.ericsson.mmaps.MapFactory;
import com.ericsson.mmaps.MapStyle;

import android.app.Activity;
import android.os.Bundle;

public class MapView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		MapFactory factory = MapFactory.getInstance();
		
		MapStyle style = new MapStyle();
		
		style.set(MapStyle.MAP_SOURCE, MapStyle.OPEN_STREET_MAP);
		
		try{
			MapView mapView = factory.createMapView(this, style, null, null, arg4);
			
		}
		catch(Exception e){
			
		}
		
	}

}
