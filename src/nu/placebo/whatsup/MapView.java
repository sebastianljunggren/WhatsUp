package nu.placebo.whatsup;

import com.ericsson.mmaps.MapFactory;

import android.app.Activity;
import android.os.Bundle;

public class MapView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		MapFactory factory = MapFactory.getInstance();
		
	}

}
