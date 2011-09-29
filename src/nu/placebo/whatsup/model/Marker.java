package nu.placebo.whatsup.model;

import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class Marker extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	
	public Marker(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
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
	public boolean onTap(int i) {
		ExtendedOverlayItem item = (ExtendedOverlayItem) overlays.get(i);
		Log.w("me", item.getId() + "");
		return true;
	}
}
