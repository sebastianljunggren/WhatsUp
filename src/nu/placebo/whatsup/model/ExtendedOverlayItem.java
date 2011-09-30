package nu.placebo.whatsup.model;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class ExtendedOverlayItem extends OverlayItem {
	
	private int id;
	
	public ExtendedOverlayItem(GeoLocation g) {
		super(g.getLocation(), g.getTitle(), null);
		id = g.getId();
	}

	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return super.getTitle();
	}
}
