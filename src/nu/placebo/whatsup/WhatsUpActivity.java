package nu.placebo.whatsup;

import android.app.Activity;
import android.os.Bundle;

import com.ericsson.mmaps.MapFactory;
import com.ericsson.mmaps.MapStyle;
import com.ericsson.mmaps.MapView;
import com.ericsson.mmaps.tools.ScaleBarTool;
import com.ericsson.mmaps.tools.TouchNavigationTool;

/**
 * 
 * The main activity of WhatsUp. Currently it displays a map.
 * 
 */

public class WhatsUpActivity extends Activity {

	private MapView mapView = null;
	private static final String DATABASE_KEY = "MDwkdrXw96IxUSfxoC051VywIfdycAVfFKtcwHJC";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MapFactory factory = MapFactory.getInstance();

		MapStyle style = new MapStyle();
		// Set the style below to use Open Street Maps.
		style.set(MapStyle.MAP_SOURCE, MapStyle.OPEN_STREET_MAP);

		try {
			mapView = factory.createMapView(this, style, new WhatsUpFontProvider(), new WhatsUpSymbolProvider(),
					DATABASE_KEY);
			setContentView(mapView);

			TouchNavigationTool touchController = new TouchNavigationTool(
					getBaseContext(), mapView, true);
			touchController.activate();
			mapView.getMapComponent().addTool(
					new ScaleBarTool(mapView.getMapComponent()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}