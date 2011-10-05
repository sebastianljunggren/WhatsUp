package nu.placebo.whatsup.model;

import java.util.ArrayList;

import nu.placebo.whatsup.activity.AnnotationActivity;
import nu.placebo.whatsup.balloon.BalloonItemizedOverlay;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * Class that contains overlays for a map, and represents them with a drawable.
 * 
 * @author Ablim
 */
public class Marker extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private Activity activity;
	
	public Marker(Drawable defaultMarker, MapView mapView, Activity a) {
		super(boundCenter(defaultMarker), mapView);
		activity = a;
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
		Intent startAnnotation = new Intent(activity, AnnotationActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("nid", ((ExtendedOverlayItem)item).getId());
		startAnnotation.putExtras(bundle);
		activity.startActivity(startAnnotation);	
		return true;
	}
	
	public void clear() {
		overlays.clear();
	}
	
}
