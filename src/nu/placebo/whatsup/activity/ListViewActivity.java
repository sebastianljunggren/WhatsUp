package nu.placebo.whatsup.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.ListMarker;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;
import nu.placebo.whatsup.network.OperationResult;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Activity showing annotations in a list view.
 * 
 * @author max
 * 
 */
public class ListViewActivity extends ListActivity implements OnClickListener,
		NetworkOperationListener<List<GeoLocation>> {

	private ArrayList<ListMarker> m_markers = null;
	private MarkerAdapter m_adapter;
	private Runnable viewMarkers;
	private GeoLocation ref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.listview);

		this.buildToolbar();
		m_markers = new ArrayList<ListMarker>();
		this.m_adapter = new MarkerAdapter(this, R.layout.list_item, m_markers);
		setListAdapter(this.m_adapter);

		this.ref = new GeoLocation(0, 57.688328, 11.979196, "Hubben");
		refresh();
	}

	private void refresh() {
		GeoLocationsRetrieve glr = new GeoLocationsRetrieve(54.826008,
				9.667969, 68.974164, 24.785156);
		glr.addOperationListener(this);
		NetworkQueue.getInstance().add(glr);
	}

	private void buildToolbar() {
		ImageButton mapBtn = (ImageButton) this.findViewById(R.id.list_goto_map);
		mapBtn.setOnClickListener(this);
		ImageButton refreshBtn = (ImageButton) this.findViewById(R.id.list_refresh);
		refreshBtn.setOnClickListener(this);
	}

	private Runnable returnRes = new Runnable() {

		public void run() {
			if (m_markers != null && m_markers.size() > 0) {
				m_adapter.markers.clear();
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < m_markers.size(); i++)
					m_adapter.add(m_markers.get(i));
			}
			// m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	public void onClick(View v) {
		if (v.getId() == R.id.list_goto_map) {
			Intent intent = new Intent(this, MapViewActivity.class);
			this.startActivity(intent);
			this.finish();
		}
		if (v.getId() == R.id.list_refresh) {
			this.refresh();
		}
	}

	public void operationExcecuted(OperationResult<List<GeoLocation>> result) {
		// TODO: Get active reference point

		if (!result.hasErrors()) {
			m_markers = new ArrayList<ListMarker>();

			for (GeoLocation item : result.getResult()) {
				m_markers.add(new ListMarker(item, ref));
			}

			Collections.sort(m_markers);
			runOnUiThread(returnRes);
		}
	}

	private class MarkerAdapter extends ArrayAdapter<ListMarker> implements
			OnClickListener {
		private Context ctx;
		private ArrayList<ListMarker> markers;

		public MarkerAdapter(Context context, int textViewResourceId,
				ArrayList<ListMarker> objects) {
			super(context, textViewResourceId, objects);
			this.markers = objects;
			this.ctx = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			ListMarker lm = markers.get(position);
			if (lm != null) {
				TextView t_title = (TextView) v
						.findViewById(R.id.list_item_title);
				TextView t_rating = (TextView) v
						.findViewById(R.id.list_item_rating);
				TextView t_range = (TextView) v
						.findViewById(R.id.list_item_range);

				if (t_title != null)
					t_title.setText(lm.getTitle());
				if (t_range != null)
					t_range.setText(lm.getRange());
				if (t_title != null)
					t_rating.setText(lm.getRating());
				v.setId(lm.getId());
				v.setOnClickListener(this);

			}
			return v;
		}

		public void onClick(View v) {
			Log.w("ListItem", "test - id: " + v.getId());
			Intent intent = new Intent(ctx, AnnotationActivity.class);
			intent.putExtra("nid", v.getId());
			ctx.startActivity(intent);
		}

	}

}
