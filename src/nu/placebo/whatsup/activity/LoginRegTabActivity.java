package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class LoginRegTabActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loginregtabhost);
		Resources res = getResources();
		TabHost host = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent(this, LogInActivity.class);
		spec = host.newTabSpec("login").setIndicator("Log in", res.getDrawable(R.drawable.user));
		spec.setContent(intent);

		host.addTab(spec);

		intent = new Intent(this, RegisterActivity.class);
		spec = host.newTabSpec("register").setIndicator("Register", res.getDrawable(R.drawable.add))
				.setContent(intent);

		host.addTab(spec);

		host.setCurrentTab(0);
	}
}
