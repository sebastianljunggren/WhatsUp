package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.model.SessionHandler;
import nu.placebo.whatsup.service.model.DataProvider;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CreateCommentActivity extends Activity implements OnClickListener {

	private int id = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id = savedInstanceState.getInt("id");
		this.setContentView(R.layout.create_comment);
		Button submitButton = (Button) this.findViewById(R.id.create_comment_submit);
		submitButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.create_comment_submit) {
			TextView text = (TextView) this.findViewById(R.id.create_commit_desc);
			TextView title = (TextView) this.findViewById(R.id.create_comment_title);
			DataProvider.getDataProvider(getApplicationContext()).createComment(
					id,
					SessionHandler.getInstance(this).getUserName(),
					text.getText().toString(),
					title.getText().toString());
		}
	}
}
