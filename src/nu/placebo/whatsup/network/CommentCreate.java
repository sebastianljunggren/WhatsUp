package nu.placebo.whatsup.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.google.android.maps.GeoPoint;

import nu.placebo.whatsup.constants.Constants;
import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.Comment;
import nu.placebo.whatsup.model.SessionInfo;

public class CommentCreate extends AbstractNetworkOperation<Comment> {
	
	private String body, title;
	private boolean hasErrors;
	private SessionInfo sessionInfo;
	private int id;
	
	public CommentCreate(String title, String body,
			int id, SessionInfo sessionInfo){
		this.id = id;
		this.title = title;
		this.body = body;
		this.sessionInfo = sessionInfo;
	}

	public OperationResult<Comment> execute() {
		Comment comment = null;
		HttpResponse response = null;
		this.hasErrors = true;
		try {
			List<NameValuePair> body = new ArrayList<NameValuePair>(2);
			body.add(new BasicNameValuePair("comment_subject", this.title));
			body.add(new BasicNameValuePair("comment_body[und][0][value]",
					this.body));
			body.add(new BasicNameValuePair("nid", Integer.toString(this.id)));
			response = NetworkCalls.performPostRequest(Constants.API_URL
					+ "comment.json", body, this.sessionInfo);
			ResponseHandler<String> handler = new BasicResponseHandler();
			comment = this.parseResult(handler.handleResponse(response));
			hasErrors = false;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new OperationResult<Comment>(hasErrors,
				response.getStatusLine().getStatusCode(), response
						.getStatusLine().getReasonPhrase(), comment);
	}

	private Comment parseResult(String handleResponse) {
		Log.d("whatsup", handleResponse);
		return null;
	}

}
