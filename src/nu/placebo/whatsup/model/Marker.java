package nu.placebo.whatsup.model;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import nu.placebo.whatsup.balloon.*;

/**
 * Class that contains overlays for a map, and represents them with a drawable.
 * 
 * @author Ablim
 */
public class Marker extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	
	public Marker(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    overlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		return true;
	}
	
}
