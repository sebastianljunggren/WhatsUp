package nu.placebo.whatsup.activity;

import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.datahandling.DataProvider;
import nu.placebo.whatsup.model.MenuHandler;
import nu.placebo.whatsup.model.ReferencePoint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RefPointActivity extends ListActivity implements OnClickListener {

	private List<ReferencePoint> refs;
	private MarkerAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.ref_point_activity);
		Log.d("whatsup", "Starting refPointActivity");

		refs = DataProvider.getDataProvider(this).getAllReferencePoints();

		String values = "";
		for (ReferencePoint rp : refs) {
			values += rp.getName() + " " + rp.getGeoPoint() + " " + rp.getId();
		}

		Log.i("Value of refs:", values);
		this.adapter = new MarkerAdapter(this, R.layout.ref_item,
				new ArrayList<ReferencePoint>());
		this.setListAdapter(adapter);

		ImageButton addBtn = (ImageButton) this
				.findViewById(R.id.add_reference);
		addBtn.setOnClickListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {

		refresh();
		super.onResume();
	}

	private void refresh() {
		refs.clear();
		refs = DataProvider.getDataProvider(this).getAllReferencePoints();
		this.adapter.fetchCurrentRefId();
		adapter.markers.clear();
		adapter.notifyDataSetChanged();
		for (int i = 0; i < refs.size(); i++) {
			adapter.add(refs.get(i));

		}
		adapter.notifyDataSetChanged();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.add_reference) {
			Intent intent = new Intent(this, PositionPickerActivity.class);
			intent.putExtra("requestCode", Constants.REFERENCE_POINT);
			this.startActivity(intent);
		}
	}

	private class MarkerAdapter extends ArrayAdapter<ReferencePoint> implements
			OnClickListener {
		private Activity ctx;
		private ArrayList<ReferencePoint> markers;
		private int currentRefId;

		public MarkerAdapter(Activity context, int textViewResourceId,
				ArrayList<ReferencePoint> objects) {
			super(context, textViewResourceId, objects);
			this.markers = objects;
			this.ctx = context;
			fetchCurrentRefId();
		}

		public void fetchCurrentRefId() {
			ReferencePoint currentRef = DataProvider.getDataProvider(ctx)
					.getCurrentReferencePoint();

			if (currentRef != null) {
				currentRefId = currentRef.getId();
			} else {
				Log.d("whatsup", "Current RefPoint == null");
				currentRefId = -2;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.ref_item, null);
			}
			ReferencePoint gl = markers.get(position);
			if (gl != null) {
				TextView t_title = (TextView) v
						.findViewById(R.id.ref_item_title);

				if (t_title != null)
					t_title.setText(gl.getName());
				ImageView iv = (ImageView) v.findViewById(R.id.current_image);
				if (gl.getId() == currentRefId) {
					iv.setVisibility(View.VISIBLE);
				} else {
					iv.setVisibility(View.INVISIBLE);
				}
				v.setId(R.id.ref_id);
				v.setTag(R.id.ref_id, gl.getId());
				ImageButton delBtn = (ImageButton) v
						.findViewById(R.id.ref_item_delete);
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
								+ ((Integer) v.getTag(R.id.ref_id)));
				DataProvider.getDataProvider(ctx).removeReferencePoint(
						(Integer) v.getTag(R.id.ref_id));
				((RefPointActivity) ctx).refresh();

			} else if (v.getId() == R.id.ref_id) {
				Log.d("whatsup",
						"Select reference point: "
								+ ((Integer) v.getTag(R.id.ref_id)));

				DataProvider.getDataProvider(ctx).setCurrentReferencePoint(
						(Integer) v.getTag(R.id.ref_id));
				ReferencePoint rp = DataProvider.getDataProvider(ctx)
						.getCurrentReferencePoint();
				Log.d("whatsup", "refPoint location: "
						+ rp.getGeoPoint().getLatitudeE6() + ", "
						+ rp.getGeoPoint().getLongitudeE6());

				ctx.finish();
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuHandler.inflate(menu, this.getMenuInflater());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuHandler.onOptionsItemSelected(item, this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return MenuHandler.onPrepareOptionsMenu(menu, this);
	}
}