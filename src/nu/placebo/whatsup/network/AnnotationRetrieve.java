package nu.placebo.whatsup.network;

import nu.placebo.whatsup.model.Annotation;

import org.json.JSONException;

public class AnnotationRetrieve extends AbstractNetworkOperation<Annotation> {
	
	private int nid;
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
		super.notifyListeners(annotation);
	}
}
