package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;
import nu.placebo.whatsup.network.OperationResult;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

/**
 * 
 * Activity that shows annotations.
 *
 */

public class AnnotationActivity extends Activity implements
		NetworkOperationListener<Annotation> {

	private TextView title;
	private TextView body;
	private TextView author;
	private Annotation annotation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.annotation);
		this.title = (TextView) this.findViewById(R.id.title);
		this.body = (TextView) this.findViewById(R.id.body);
		this.author = (TextView) this.findViewById(R.id.author);
		Annotation a = new Annotation(new GeoLocation(1234, 23, 32,
				"The title of my annotation!"), "Brödtext", "Sebbe", null);
		this.setAnnotation(a);
		Bundle bundle = getIntent().getExtras();
		AnnotationRetrieve ar = new AnnotationRetrieve(bundle.getInt("nid"));
		ar.addOperationListener(this);
		NetworkQueue.getInstance().add(ar);
	}

	public Annotation getAnnotation() {
		return this.annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
		this.title.setText(annotation.getGeoLocation().getTitle());
		this.body.setText(Html.fromHtml(annotation.getBody()));
		this.author.setText("by " + annotation.getAuthor());
	}

	public void operationExcecuted(final OperationResult<Annotation> result) {
		if (!result.hasErrors()) {
			this.runOnUiThread(new Runnable() {

				public void run() {
					setAnnotation(result.getResult());

				}

			});
		}
	}

}
