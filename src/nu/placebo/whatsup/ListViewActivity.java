package nu.placebo.whatsup;

import java.util.ArrayList;
import java.util.List;

import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.ListMarker;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
/**
 * Activity showing annotations in a list view.
 * 
 * @author max
 *
 */
public class ListViewActivity extends ListActivity {
	
	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<ListMarker> m_markers = null;
	private MarkerAdapter m_adapter;
	private Runnable viewOrders;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.listview);
		
		GeoLocation ref = new GeoLocation(0, 57.688337, 11.979132, "The Hubben");
		
		this.m_markers = new ArrayList<ListMarker>();
		m_markers.add(new ListMarker(new GeoLocation(1, 56, 12, "Test"), ref));
		
	}
	
	
	
	
	
	
	private class MarkerAdapter extends ArrayAdapter<ListMarker> {
		
		private ArrayList<ListMarker> markers;
		public MarkerAdapter(Context context, int textViewResourceId,
				ArrayList<ListMarker> objects) {
			super(context, textViewResourceId, objects);
			this.markers = objects;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null){
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			ListMarker lm = markers.get(position);
			if(lm != null){
				TextView t_title = (TextView) v.findViewById(R.id.list_item_title);
				TextView t_rating= (TextView) v.findViewById(R.id.list_item_rating);
				TextView t_range = (TextView) v.findViewById(R.id.list_item_range);
				
				if(t_title != null)
					t_title.setText(lm.getTitle());
				if(t_range != null)
					t_range.setText(lm.getRange());
				if(t_title != null)
					t_title.setText(lm.getRating());
				
			}
			
			return v;
		}
		
		
	}
}
