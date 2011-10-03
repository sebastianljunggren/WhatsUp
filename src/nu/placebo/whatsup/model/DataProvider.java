package nu.placebo.whatsup.model;

import java.util.List;

import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;

public class DataProvider {

	/**
	 * Class is Singelton, disallow creation
	 */
	private DataProvider() {
	}

	private static final DataProvider instance = new DataProvider();

	private DatabaseHelper dbHelper = new DatabaseHelper(null);
	private NetworkQueue networkQueue = NetworkQueue.getInstance();

	public static DataProvider getDataProvider() {
		return instance;
	}

	public Annotation getAnnotation(int nid) {
		// Fetches an annotation from the local database.
		return null;
	}

	public List<GeoLocation> getAnnotationMarkers(double latitudeA,
			double longitudeA, double latitudeB, double longitudeB) {
		// Fetches GeoLocations from the local database.
		return null;
	}

	public void retrieveAnnotation(int nid) {
		// Fetches an annotation from the server and stores it in the local
		// database.
		AnnotationRetrieve ar = new AnnotationRetrieve(nid);
		ar.addOperationListener(new NetworkOperationListener<Annotation>() {

			public void operationExcecuted(Annotation result) {
				// TODO Store in local db
				// TODO Notify those interested in the result (binder).				
			}
			
		});
		this.networkQueue.add(ar);

	}

	public void retrieveGeoLocations(double latitudeA, double longitudeA,
			double latitudeB, double longitudeB) {
		// Fetches GeoLocations from the server and stores them in the local
		// database.
		GeoLocationsRetrieve gr = new GeoLocationsRetrieve(latitudeA,
				longitudeA, latitudeB, longitudeB);
		gr.addOperationListener(new NetworkOperationListener<List<GeoLocation>>() {

			public void operationExcecuted(List<GeoLocation> result) {
				// TODO Store in local db
				// TODO Notify those interested in the result (binder).
			}

		});
		this.networkQueue.add(gr);
	}
}
