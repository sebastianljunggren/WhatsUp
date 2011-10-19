package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.ctrl.SessionHandler;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 *
 * Handles the menu of activities since they all have the same menu.
 *
 */

public class MenuHelper {
	public static boolean onOptionsItemSelected(MenuItem item, Context context) {
		switch (item.getItemId()) {
		case R.id.menu_log_in_out:
			SessionHandler sh = SessionHandler.getInstance(context);
			if(sh.hasSession()){
				sh.logOut();
			} else {
				context.startActivity(new Intent(context, LoginRegTabActivity.class));
			}
			return true;
		case R.id.menu_options:
			Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return false;
		}
	}

	public static void inflate(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu, menu);
	}

	public static boolean onPrepareOptionsMenu(Menu menu, Context context) {
		MenuItem logInOut = menu.getItem(0);
		if(SessionHandler.getInstance(context).hasSession()) {
			logInOut.setTitle("Log out" );
			logInOut.setIcon(R.drawable.logout);
		} else {
			logInOut.setTitle("Log in/register");
			logInOut.setIcon(R.drawable.user);
		}
		return true;
	}
}
