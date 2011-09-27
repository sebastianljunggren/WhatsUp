package nu.placebo.whatsup.network;

public class AnnotationMarkerQueueItem implements NetworkQueueItem {
	private double latitudeA;
	private double longitudeA;
	private double latitudeB;
	private double longitudeB;

	public AnnotationMarkerQueueItem(double latitudeA, double longitudeA,
			double latitudeB, double longitudeB) {
		this.latitudeA = latitudeA;
		this.longitudeA = longitudeA;
		this.latitudeB = latitudeB;
		this.longitudeB = longitudeB;
	}

	public String execute() {
		return NetworkCalls.retrieveAnnotationMarkers(latitudeA, longitudeA,
				latitudeB, longitudeB);
	}

}
