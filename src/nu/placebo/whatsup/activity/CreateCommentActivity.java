package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.Comment;
import nu.placebo.whatsup.model.SessionHandler;
import nu.placebo.whatsup.network.CommentCreate;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkTask;
import nu.placebo.whatsup.network.OperationResult;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCommentActivity extends Activity implements OnClickListener,
		NetworkOperationListener<Comment> {

	private int id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.create_comment);
		Bundle extras = this.getIntent().getExtras();
		this.id = extras.getInt("id");
		Button submitButton = (Button) this
				.findViewById(R.id.create_comment_submit);
		submitButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.create_comment_submit) {
			TextView text = (TextView) this
					.findViewById(R.id.create_commit_desc);
			TextView title = (TextView) this
					.findViewById(R.id.create_comment_title);
			if (text.toString() != "" && title.toString() != "") {
				/*
				 * DataProvider.getDataProvider(getApplicationContext()).
				 * createComment( id,
				 * SessionHandler.getInstance(this).getUserName(),
				 * text.getText().toString(), title.getText().toString());
				 */

				// Try direct network operation
				CommentCreate cc = new CommentCreate(title.getText().toString(),
						text.getText().toString(), id, SessionHandler.getInstance(this)
								.getSession());
				cc.addOperationListener(this);
				new NetworkTask<Comment>().execute(cc);

			} else {
				Toast.makeText(this, "Please enter text in both fields",
						Toast.LENGTH_LONG);
			}
		}
	}

	public void operationExcecuted(OperationResult<Comment> result) {
		// TODO Error handling
		if (!result.hasErrors()) {
			setResult(Constants.ACTIVITY_FINISHED_OK);
			Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT);
			this.finish();
		}
	}
}
