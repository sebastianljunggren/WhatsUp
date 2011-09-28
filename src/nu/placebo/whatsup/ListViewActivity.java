package nu.placebo.whatsup;

import java.util.List;

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

		public MarkerAdapter(Context context, int textViewResourceId,
				List<ListMarker> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
	}
}
