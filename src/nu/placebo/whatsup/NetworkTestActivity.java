package nu.placebo.whatsup;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.network.NetworkCalls;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NetworkTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.network_test_activity);
		final TextView tv1 = (TextView) this.findViewById(R.id.networkResult1);
		final TextView tv2 = (TextView) this.findViewById(R.id.neworkResult2);
		tv1.setText("hej");
		tv2.setText(NetworkCalls.retrieveAnnotation(103));
	}

}
