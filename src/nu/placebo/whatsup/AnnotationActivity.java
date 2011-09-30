package nu.placebo.whatsup;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.GeoLocation;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AnnotationActivity extends Activity {

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
				"The title of my annotation!"), "Brödtext", 1337, "Sebbe");
		this.setAnnotation(a);
	}

	public Annotation getAnnotation() {
		return this.annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
		this.title.setText(annotation.getGeoLocation().getTitle());
		this.body.setText(annotation.getBody());
		this.author.setText("by " + annotation.getAuthor());
	}

}
