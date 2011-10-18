package nu.placebo.whatsup.datahandling;

import java.util.ArrayList;
import java.util.List;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.ReferencePoint;
import nu.placebo.whatsup.model.SessionInfo;
import nu.placebo.whatsup.network.AnnotationCreate;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkTask;
import nu.placebo.whatsup.network.OperationResult;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;

public class DataProvider implements NetworkOperationListener<Annotation>, LocationListener {
	
	/**
	 * Class is Singelton, disallow outside creation
	 */
	private DataProvider(Context c) {
		DatabaseConnectionLayer.setDatabaseHelper(new DatabaseHelper(c));
		List<GeoLocation> glList = new ArrayList<GeoLocation>();
		Location lastKnownLocation = ((LocationManager) c.getSystemService(Context.LOCATION_SERVICE)).
													getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(lastKnownLocation != null) {
			glList.add(new GeoLocation(-1,
					lastKnownLocation.getLatitude(),
					lastKnownLocation.getLongitude(),
					"physical_position"));
			insertData(glList);
		}
	}
	
	private static volatile DataProvider instance;

	public static DataProvider getDataProvider(Context c) {
		if(instance == null) {
			instance = new DataProvider(c);
		}
		return instance;
	}
	
	/**
	 * This method supplies the caller with a DataReturn<Annotation> object that will
	 * contain an the annotation with the given id, if it has previously been saved to
	 * the database. If it was not found, the local data will be null, and the requester
	 * will have to wait until new server data is available.
	 * 
	 * @param nid
	 * @return
	 */
	public DataReturn<Annotation> getAnnotation(int nid) {
		
		DataReturn<Annotation> result;
		
		result = new DataReturn<Annotation>(DatabaseConnectionLayer.getAnnotation(nid),
						activeObjects.size());	
		activeObjects.add(result);
		AnnotationRetrieve ar = new AnnotationRetrieve(nid);
		ar.addOperationListener(result);
		new NetworkTask<Annotation>().execute(ar);
		return result;
	}
	
	/**
	 * Convenience method for getAnnotationMarkers(int, int, int, int) when you have two
	 * locations.
	 * 
	 * @param a one of the points
	 * @param b the other point
	 * @return 
	 */
	public DataReturn<List<GeoLocation>> getAnnotationMarkers(GeoLocation a, GeoLocation b) {
		return getAnnotationMarkers(a.getLocation().getLatitudeE6(), 
									a.getLocation().getLongitudeE6(),
									b.getLocation().getLatitudeE6(),
									b.getLocation().getLongitudeE6());
	}
	
	/**
	 * Calling this method is a request to get GeoLocations within
	 * the rectangular area given by applying the parameters 
	 * constraints.
	 * 
	 * @param latitudeA the latitude of the first point, in microlatitude
	 * @param longitudeA the longitude of the first point, in microlongitude
	 * @param latitudeB the latitude of the second point, in microlatitude
	 * @param longitudeB the longitude of the second point, in microlngitude
	 * @return a DataReturn object containing any local data found and which can
	 * be listened to by
	 */
	
	public DataReturn<List<GeoLocation>> getAnnotationMarkers(int latitudeA,
			int longitudeA, int latitudeB, int longitudeB) {
		int maxLat = Math.max(latitudeA, latitudeB);
		int maxLong = Math.max(longitudeA, longitudeB);
		int minLat = Math.min(latitudeA, latitudeB);
		int minLong = Math.min(longitudeA, longitudeB);

		DataReturn<List<GeoLocation>> result;
		GeoLocationsRetrieve glr = new GeoLocationsRetrieve(
				(latitudeA - 0.5) / 1000000, (longitudeA - 0.5) / 1000000,
				(latitudeB - 0.5) / 1000000, (longitudeB - 0.5) / 1000000);
		
		synchronized(this) {
			result = new DataReturn<List<GeoLocation>>(DatabaseConnectionLayer.getAnnotationMarkers(
											maxLat, maxLong, minLat, minLong), activeObjects.size());
			activeObjects.add(result);
		}
		glr.addOperationListener(result);
		new NetworkTask<List<GeoLocation>>().execute(glr);
		return result;
	}
	
	/**
	 * Gets the ReferencePoint that is currently used as reference point.
	 * In the case that the user has not chosen a reference point, the physical
	 * position of the phone is returned.
	 * 
	 * @return the current reference point, as a ReferencePoint object.
	 */
	public ReferencePoint getCurrentReferencePoint() {
		return currentReferencePoint;
	}
	
	/**
	 * Returns all reference points saved in the database, and the physical
	 * position of the phone, which is always a reference point.
	 * 
	 * @return all reference point, including the physical location of the phone.
	 */
	public List<ReferencePoint> getAllReferencePoints() {
		return DatabaseConnectionLayer.getAllReferencePoints();
	}
	
	/**
	 * Sets the ReferencePoint to use as the current reference point, both short-time
	 * and in the database.
	 * 
	 * @param id the id of the already existing reference point. If the id does not
	 * match any existing reference point, nothing changes.
	 */
	public void setCurrentReferencePoint(int id) {
		currentReferencePoint = DatabaseConnectionLayer.setCurrentReferencePoint(id);
	}
	
	/**
	 * Adds a reference point to the database.
	 * 
	 * @param gp the geographical location of the reference point.
	 * @param name the name of the reference point.
	 */
	public void addReferencePoint(GeoPoint gp, String name) {
		DatabaseConnectionLayer.addReferencePoint(gp, name);

	}
		
	/**
	 * Removes a reference point from the database.
	 * 
	 * @param gl the point to remove
	 */
	public void removeReferencePoint(int id) {
		DatabaseConnectionLayer.removeReferencePoint(id);
	}
	
	/**
	 * Creates an Annotation with the specified values. A valid SessionInfo is required, and the object
	 * that wants the annotation should be sent as listener. The listener can be null if no object wants the
	 * returned Annotation immediately.
	 * 
	 * When the object is returned, the DataProvider will receive it as well, and it will be put into the
	 * database. The time until the annotation is stably in the database is determined by the workload of the
	 * phone, threading priority and the time a SQLite insert takes. As this is mostly random, trying to get
	 * this annotation from the database before it's fully updated will result
	 * 
	 * @param title
	 * @param desc
	 * @param author
	 * @param gp
	 * @param sInfo
	 * @param listener
	 */
	public void createAnnotation(String title, String desc, String author, 
			GeoPoint gp, SessionInfo sInfo, 
			NetworkOperationListener<Annotation> listener) {
		AnnotationCreate ac = new AnnotationCreate(title, desc, author, gp, sInfo);
		if(listener != null) {
			ac.addOperationListener(listener);
		}
		new NetworkTask<Annotation>().execute(ac);
	}
	
	public void createComment(int nid, String author, String commentText, String title) {
		DatabaseConnectionLayer.storeComment(nid, author, commentText, title);
	}
	
	/**
	 * Called by DataReturn object to notify the DataProvider about new data
	 * to insert into the local database.
	 * 
	 * @param newData whether the data differs from that in the local database
	 * @param id the id of the DataReturn that has finished
	 */
	@SuppressWarnings("unchecked")
	void newDataRecieved(boolean newData, int id) {
		if(newData) {
			OperationResult<?> result = activeObjects.get(id).getNewData();
			
			if(result == null || result.getResult() == null) {
				return;
			}
			if(!result.hasErrors()) {
				Object data = result.getResult();
				//Test what type the new data has		
				if(data.getClass() == Annotation.class) {
					if(!insertData((Annotation) data)) {
						//TODO Error handling
					}
				} else if(data.getClass() == List.class) {
					if(!insertData((List<GeoLocation>) data)) {
						//TODO Error handling
					}
				}
			}
		}
		activeObjects.remove(id);
	}
	
	//Inserts the given Annotation into its table, and it's auxiliary
	//information into their tables. If error occurs, false is returned.
	private boolean insertData(Annotation a) {
		return DatabaseConnectionLayer.storeAnnotation(a);
	}

	//Inserts the given list with GeoLocations into its table.
	//If error occurs, false is returned.
	private boolean insertData(List<GeoLocation> glList) {
		return DatabaseConnectionLayer.storeGeoLocations(glList);
	}

	private List<DataReturn<?>> activeObjects = new ArrayList<DataReturn<?>>();
	private ReferencePoint currentReferencePoint;

	public void operationExcecuted(OperationResult<Annotation> result) {
		if(!result.hasErrors()) {
			insertData(result.getResult());
		}
	}

	public void onLocationChanged(Location location) {
		DatabaseConnectionLayer.updateCurrentLocation(
				new GeoPoint((int)location.getLatitude(), (int)location.getLongitude()));
;	}
	public void onProviderDisabled(String provider) {}
	public void onProviderEnabled(String provider) {}
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}
