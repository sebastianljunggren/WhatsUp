package nu.placebo.whatsup.network;

import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.model.Annotation;

import org.json.JSONException;

public class AnnotationRetrieve implements NetworkOperation<Annotation> {
	
	private int nid;
	private List<NetworkOperationListener<Annotation>> listeners = new ArrayList<NetworkOperationListener<Annotation>>();

	public AnnotationRetrieve(int nid) {
		this.nid = nid;
	}

	public void execute() {
		Annotation annotation = null;
		try {
			annotation = new Annotation(NetworkCalls.retrieveAnnotation(nid));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for(NetworkOperationListener<Annotation> listener: this.listeners) {
			listener.operationExcecuted(annotation);
		}
	}

	public void addOperationListener(NetworkOperationListener<Annotation> listener) {
		this.listeners.add(listener);
	}
}
