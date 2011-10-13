package nu.placebo.whatsup.activity;

import nu.placebo.whatsup.R;
import nu.placebo.whatsup.model.SessionHandler;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkTask;
import nu.placebo.whatsup.network.OperationResult;
import nu.placebo.whatsup.network.RegisterOperation;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener, NetworkOperationListener<Void>{
	
	private String username;
	private String password;
	private String email;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.register);
		
		Button submitBtn = (Button) this.findViewById(R.id.submit);
		submitBtn.setOnClickListener(this);
		username = "";
		password = "";
		email = "";
	}

	public void onClick(View v) {
		if(v.getId() == R.id.submit){
			String username = ((TextView) this.findViewById(R.id.user_name)).getText().toString();
			String password = ((TextView) this.findViewById(R.id.password)).getText().toString();
			String email = ((TextView) this.findViewById(R.id.email)).getText().toString();
			
			if(username != "" && password != "" && email !=""){
				RegisterOperation ro = new RegisterOperation(username, password, email);
				ro.addOperationListener(this);
				new NetworkTask<Void>().execute(ro);
			}
		}
		
	}

	public void operationExcecuted(OperationResult<Void> result) {
		if(!result.hasErrors()){
			Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
			
			SessionHandler.getInstance(this).saveCredentials(username, password);
			SessionHandler.getInstance(this).login();
			
			this.finish();
		}
		
	}
}
