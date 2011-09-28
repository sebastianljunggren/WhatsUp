package nu.placebo.whatsup;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NetworkTestActivity extends Activity implements NetworkOperationListener<Annotation>{

	private TextView tv1;
	private TextView tv2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.network_test_activity);
		this.tv1 = (TextView) this.findViewById(R.id.networkResult1);
		this.tv2 = (TextView) this.findViewById(R.id.networkResult2);
		AnnotationRetrieve at = new AnnotationRetrieve(1234);
		at.addOperationListener(this);
		NetworkQueue.getInstance().add(at);
	}

	public void operationExcecuted(Annotation result) {
		tv1.setText(result.getGeolocation().getTitle());
		tv2.setText(result.getBody());
		
	}

}
