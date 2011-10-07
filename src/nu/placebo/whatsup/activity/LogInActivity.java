package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.model.SessionInfo;
import nu.placebo.whatsup.network.Login;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;
import nu.placebo.whatsup.network.OperationResult;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * Activity with the log in form.
 *
 */

public class LogInActivity extends Activity implements OnClickListener, NetworkOperationListener<SessionInfo> {

	private TextView userName;
	private TextView password;
	private Button logIn;
	private TextView result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);
		this.userName = (TextView) this.findViewById(R.id.user_name);
		this.password = (TextView) this.findViewById(R.id.password);
		this.logIn = (Button) this.findViewById(R.id.log_in);
		this.logIn.setOnClickListener(this);
		this.result = (TextView) this.findViewById(R.id.result);
	}

	public void onClick(View view) {
		this.result.setText("User: " + this.userName.getText() + " Password: "
				+ password.getText());
		Login login = new Login(this.userName.getText().toString(),
				this.password.getText().toString());
		login.addOperationListener(this);
		NetworkQueue.getInstance().add(login);
		this.result.setText("Logging in...");
	}

	public void operationExcecuted(final OperationResult<SessionInfo> r) {
		this.runOnUiThread(new Runnable() {

			public void run() {
				if (!r.hasErrors()) {
					result.setText("Successfully logged in!");
				} else {
					result.setText("Failed to login: " + r.getStatusMessage());
				}
			}
			
		});
	}
}
