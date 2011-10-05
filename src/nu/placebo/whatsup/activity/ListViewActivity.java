package nu.placebo.whatsup.activity;

import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.R.id;
import nu.placebo.whatsup.R.layout;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.ListMarker;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
/**
 * Activity showing annotations in a list view.
 * 
 * @author max
 *
 */
public class ListViewActivity extends ListActivity implements OnClickListener, NetworkOperationListener<List<GeoLocation>> {
	
	private ArrayList<ListMarker> m_markers = null;
	private MarkerAdapter m_adapter;
	private Runnable viewMarkers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.listview);
		
		this.buildToolbar();
		m_markers = new ArrayList<ListMarker>();
		this.m_adapter = new MarkerAdapter(this, R.layout.list_item, m_markers);
		setListAdapter(this.m_adapter);
		
	//	GeoLocationsRetrieve glr = new GeoLocationsRetrieve(0, 0, 0, 0);
		
		
/*		TODO: May be removed
		viewMarkers = new Runnable(){
			public void run(){
				getMarkers();
			}
		};
		Thread thread = new Thread(null, viewMarkers, "MagnetoBackground");
		thread.start(); */		
		
	}
	
	private void buildToolbar() {
		Button mapBtn = (Button) this.findViewById(R.id.list_goto_map);
		mapBtn.setOnClickListener(this);
		
	}
/*		TODO: May be removed, handled by operationExecuted()
	private void getMarkers(){
		try{
			GeoLocation ref = new GeoLocation(0, 57.688337, 11.979132, "The Hubben");
			m_markers = new ArrayList<ListMarker>();
			
			m_markers.add(new ListMarker(new GeoLocation(1, 57.706325, 11.937160, "Lindholmen"), ref));
			m_markers.add(new ListMarker(new GeoLocation(2, 57.706325, 11.937160, "Liseberg"), ref));
		} catch (Exception e) {
			
		}
		runOnUiThread(returnRes);
	}
*/	
	private Runnable returnRes = new Runnable() {

		public void run() {
			if(m_markers != null && m_markers.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_markers.size();i++)
                m_adapter.add(m_markers.get(i));
            }
           // m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();			
		}
    };
	
    public void onClick(View v){
    	if(v.getId() == R.id.list_goto_map){
    		Intent intent = new Intent(this, MapViewActivity.class);
    		this.startActivity(intent);
    		this.finish();
    	}
    }
    
    public void operationExcecuted(List<GeoLocation> result) {
    	// Get active reference point
    	GeoLocation ref = new GeoLocation(0, 57.688328, 11.979196, "Hubben");
    	m_markers = new ArrayList<ListMarker>();
    	
		for(GeoLocation item : result){
			m_markers.add(new ListMarker(item, ref));
		}
		runOnUiThread(returnRes);
	}
	
	private class MarkerAdapter extends ArrayAdapter<ListMarker> implements OnClickListener{
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
					t_rating.setText(lm.getRating());
				v.setId(lm.getId());
				v.setOnClickListener(this);
				
			}
			return v;
		}
		
		public void onClick(View v) {
			Log.w("ListItem", "test - id: "+ v.getId());
			Intent intent = new Intent(ctx, AnnotationActivity.class);
			intent.putExtra("nid", v.getId());
			ctx.startActivity(intent);
		}


		
		
		
		
	}

	
}
