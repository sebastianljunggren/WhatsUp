package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkTask;
import nu.placebo.whatsup.network.OperationResult;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


/**
 * 
 * Activity that shows annotations.
 *
 */

public class AnnotationActivity extends Activity implements
		OnClickListener, NetworkOperationListener<Annotation> {

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
		Bundle bundle = getIntent().getExtras();
		AnnotationRetrieve ar = new AnnotationRetrieve(bundle.getInt("nid"));
		ar.addOperationListener(this);
		new NetworkTask<Annotation>().execute(ar);
	//	Button commentButton = (Button) this.findViewById(R.id.comment);
	//	commentButton.setOnClickListener(this);
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
			setAnnotation(result.getResult());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuHelper.inflate(menu, this.getMenuInflater());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return MenuHelper.onOptionsItemSelected(item, this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return MenuHelper.onPrepareOptionsMenu(menu, this);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.comment) { 			// NOT YET READY FOR RELEASE
			Intent intent = new Intent(this, CreateCommentActivity.class);
			intent.putExtra("id", annotation.getId());
			this.startActivity(intent);
		} 

	}
}
