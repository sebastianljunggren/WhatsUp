package nu.placebo.whatsup.model;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class ExtendedOverlayItem extends OverlayItem {
	
	private int id;
	
	public ExtendedOverlayItem(GeoPoint p, String title, int id) {
		super(p, title, null);
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return super.getTitle();
	}
}
