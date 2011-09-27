package nu.placebo.whatsup.network;

public class AnnotationQueueItem implements NetworkQueueItem {
	
	private int nid;

	public AnnotationQueueItem(int nid) {
		this.nid = nid;
	}

	public String execute() {
		return NetworkCalls.retrieveAnnotation(nid);
	}
}
