package nu.placebo.whatsup.activity;

import java.util.ArrayList;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.ListMarker;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class RefPointActivity extends ListActivity {
	
	private ArrayList<GeoLocation> refs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ref_point_activity);
		
		refs = new ArrayList<GeoLocation>();
		MarkerAdapter adapter = new MarkerAdapter(this, R.layout.ref_item, refs);
		this.setListAdapter(adapter);
		
		
		refs.add(new GeoLocation(1, 57708759, 11937507, "Lindholmen"));
		refs.add(new GeoLocation(2, 57687959, 11978865, "Linsen"));
		
		adapter.clear();
		for(int i=0; i< refs.size(); i++){
			adapter.add(refs.get(i));
		}
		adapter.notifyDataSetChanged();
		
		
	}
	
	private class MarkerAdapter extends ArrayAdapter<GeoLocation> implements
			OnClickListener {
		private Context ctx;
		private ArrayList<GeoLocation> markers;

		public MarkerAdapter(Context context, int textViewResourceId,
				ArrayList<GeoLocation> objects) {
			super(context, textViewResourceId, objects);
			this.markers = objects;
			this.ctx = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.ref_item, null);
			}
			GeoLocation gl = markers.get(position);
			if (gl != null) {
				TextView t_title = (TextView) v
						.findViewById(R.id.ref_item_delete);

				if (t_title != null)
					t_title.setText(gl.getTitle());
				v.setId(Constants.REFERENCE_POINT);
				v.setTag(Constants.REFERENCE_POINT, gl.getId());
				ImageButton delBtn = (ImageButton) v.findViewById(R.id.ref_item_delete);
				delBtn.setTag(Constants.REFERENCE_POINT, gl.getId());
				delBtn.setOnClickListener(this);
				v.setOnClickListener(this);

			}
			return v;
		}

		public void onClick(View v) {
			if (v.getId() == R.id.ref_item_delete) {
				Log.d("whatsup",
						"Delete reference point: "
								+ ((Integer) v
										.getTag(Constants.REFERENCE_POINT)));
			} else if (v.getId() == Constants.REFERENCE_POINT) {
				Log.d("whatsup",
						"Select reference point: "
								+ ((Integer) v
										.getTag(Constants.REFERENCE_POINT)));
			}

		}

	}
}
