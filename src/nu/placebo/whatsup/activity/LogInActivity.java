package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LogInActivity extends Activity implements OnClickListener {

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
		this.result.setText("User: " + this.userName.getText()
				+ " Password: " + password.getText());
	}
}
