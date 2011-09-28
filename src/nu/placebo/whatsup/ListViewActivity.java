package nu.placebo.whatsup;

import java.util.ArrayList;
import java.util.List;

import android.view.*;
import android.widget.ArrayAdapter;
import nu.placebo.whatsup.model.ListMarker;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
/**
 * Activity showing annotations in a list view.
 * 
 * @author max
 *
 */
public class ListViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.listview);
	}
	
	
	
	
	
	
	private class MarkerAdapter extends ArrayAdapter<ListMarker> {
		
		private ArrayList<ListMarker> markers;
		public MarkerAdapter(Context context, int textViewResourceId,
				List<ListMarker> objects) {
			super(context, textViewResourceId, objects);
			
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
				// Find text views and fill them with info from ListMarker
			}
			
			return v;
		}
		
		
	}
}
