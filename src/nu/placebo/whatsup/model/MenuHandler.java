package nu.placebo.whatsup.model;

import nu.placebo.whatsup.R;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuHandler {
	public static boolean onOptionsItemSelected(MenuItem item, Context context) {
	    Toast.makeText(context, "Menu tapped!", Toast.LENGTH_SHORT).show();
	    switch (item.getItemId()) {
	    case R.id.menu_log_in:
	        return true;
	    case R.id.menu_log_out:
	        return true;
	    case R.id.menu_options:
	        return true;
	    default:
	        return false;
	    }
	}
}
