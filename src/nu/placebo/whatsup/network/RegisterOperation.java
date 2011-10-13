package nu.placebo.whatsup.network;

import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class RegisterOperation extends AbstractNetworkOperation<Void> {

	private String userName;
	private String passWord;
	private String email;
	private boolean hasErrors;

	public RegisterOperation(String userName, String passWord, String email) {
		this.email = email;
		this.userName = userName;
		this.passWord = passWord;
	}

	public OperationResult<Void> execute() {
		HttpResponse response = null;
		this.hasErrors = true;

		List<NameValuePair> body = new ArrayList<NameValuePair>();
		body.add(new BasicNameValuePair("account[name]", this.userName));
		body.add(new BasicNameValuePair("account[mail]", this.email));
		body.add(new BasicNameValuePair("account[pass]", this.passWord));
		response = NetworkCalls.performPostRequest(Constants.API_URL
				+ "user.json", body, null);
		hasErrors = false;

		return new OperationResult<Void>(hasErrors, response.getStatusLine()
				.getStatusCode(), response.getStatusLine().getReasonPhrase(),
				null);
	}
}
