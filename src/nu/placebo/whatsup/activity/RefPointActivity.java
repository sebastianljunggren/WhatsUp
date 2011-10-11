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
	private MarkerAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ref_point_activity);
		Log.d("whatsup", "Starting refPointActivity");
		
		
		
		refs = new ArrayList<GeoLocation>();
		refs.add(new GeoLocation(1, 57708759, 11937507, "Lindholmen"));
		refs.add(new GeoLocation(2, 57687959, 11978865, "Linsen"));
		
		this.adapter = new MarkerAdapter(this, R.layout.ref_item, new ArrayList<GeoLocation>());
		this.setListAdapter(adapter);
		
		adapter.markers.clear();
		adapter.notifyDataSetChanged();
		for (int i = 0; i < refs.size(); i++){
			adapter.add(refs.get(i));
			
		}
		adapter.notifyDataSetChanged();
		
	//	this.runOnUiThread(returnRes);
		
		
	}
	
	private Runnable returnRes = new Runnable() {

		public void run() {
			if (refs != null && refs.size() > 0) {
				
			}
			// m_ProgressDialog.dismiss();
			
		}
	};
	
	
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
						.findViewById(R.id.ref_item_title);

				if (t_title != null)
					t_title.setText(gl.getTitle());
				v.setId(R.id.ref_id);
				v.setTag(R.id.ref_id, gl.getId());
				ImageButton delBtn = (ImageButton) v.findViewById(R.id.ref_item_delete);
				delBtn.setTag(R.id.ref_id, gl.getId());
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
										.getTag(R.id.ref_id)));
			} else if (v.getId() == R.id.ref_id) {
				Log.d("whatsup",
						"Select reference point: "
								+ ((Integer) v
										.getTag(R.id.ref_id)));
			}

		}

	}
}
