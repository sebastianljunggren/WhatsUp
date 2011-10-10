package nu.placebo.whatsup.model;

import com.google.android.maps.OverlayItem;

/**
 * An extended OverlayItem to contain an id.
 * 
 * @author Ablim
 */
public class ExtendedOverlayItem extends OverlayItem {
	
	private int id;
	
	public ExtendedOverlayItem(GeoLocation g) {
		super(g.getLocation(), g.getTitle(), "Rating: 0");
		id = g.getId();
	}

	public int getId() {
		return id;
	}
}
