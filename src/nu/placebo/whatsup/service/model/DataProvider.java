package nu.placebo.whatsup.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.Comment;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.ReferencePoint;
import nu.placebo.whatsup.model.SessionInfo;
import nu.placebo.whatsup.network.AnnotationCreate;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkTask;
import nu.placebo.whatsup.network.OperationResult;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class DataProvider implements NetworkOperationListener<Annotation>, LocationListener {
	
	/**
	 * Class is Singelton, disallow outside creation
	 */
	private DataProvider(Context c) {
		dbHelper = new DatabaseHelper(c);
		List<GeoLocation> glList = new ArrayList<GeoLocation>();
		Location lastKnownLocation = ((LocationManager) c.getSystemService(Context.LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);
		glList.add(new GeoLocation(-1,
				lastKnownLocation.getLatitude(),
				lastKnownLocation.getLongitude(),
				"physical_position"));
		insertData(glList);
	}
	
	private static volatile DataProvider instance;

	private DatabaseHelper dbHelper;

	public static DataProvider getDataProvider(Context c) {
		if(instance == null) {
			instance = new DataProvider(c);
		}
		return instance;
	}
	
	public DataReturn<Annotation> getAnnotation(int nid) {
		//Calling local cache first, then putting a network call in the queue, returning an object containing the content of the local
		//cache, and means of acquiring the data fetched from remote server
		
		Cursor c = dbHelper.getReadableDatabase().query(
				DatabaseHelper.ANNOTATION_TABLE, 
				null, 
				"nid = " + nid, 
				null, 
				null, 
				null, 
				null);
		
		c.moveToPosition(0);
		String author = c.getString(c.getColumnIndex("author"));
		String body = c.getString(c.getColumnIndex("body"));
		
		c = dbHelper.getReadableDatabase().query(
				DatabaseHelper.GEOLOCATION_TABLE, 
				null, 
				"nid = " + nid, 
				null, 
				null, 
				null, 
				null);
		
		c.moveToPosition(0);
		int latitude = c.getInt(c.getColumnIndex("latitude"));
		int longitude = c.getInt(c.getColumnIndex("longitude"));
		String title = c.getString(c.getColumnIndex("title"));
		
		c = dbHelper.getReadableDatabase().query(
				DatabaseHelper.COMMENT_TABLE, 
				null, 
				null, 
				null, 
				null, 
				null, 
				null);
		
		c.moveToPosition(0);
		List<Comment> comments = new ArrayList<Comment>();
		do {
			String comment = c.getString(c.getColumnIndex("comment"));
			String cTitle = c.getString(c.getColumnIndex("title"));
			String cAuthor = c.getString(c.getColumnIndex("author"));
			Date cDate = new Date(c.getLong(c.getColumnIndex("added_date")));
			comments.add(new Comment(cAuthor, comment, cTitle, cDate));
		} while(!c.isLast());
		
		c.close();
		DataReturn<Annotation> result;
		
		synchronized(this) {
			result = new DataReturn<Annotation>(new Annotation(
				new GeoLocation(nid, latitude, longitude, title), 
				body, author, comments), activeObjects.size());
		}
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
	 * the rectangular area 
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
		//Database part here
		List<GeoLocation> locations = new ArrayList<GeoLocation>();
		String maxLat = Double.toHexString(Math.max(latitudeA, latitudeB));
		String maxLong = Double.toHexString(Math.max(longitudeA, longitudeB));
		String minLat = Double.toHexString(Math.min(latitudeA, latitudeB));
		String minLong = Double.toHexString(Math.min(longitudeA, longitudeB));
		String[] selectionArgs = {maxLat, maxLong, minLat, minLong};
		
		Cursor c = dbHelper.getReadableDatabase().query(DatabaseHelper.GEOLOCATION_TABLE,
				null,
				"latitude < ? AND longitude < ? AND latitude > ? AND longitude > ?",
				selectionArgs,
				null,
				null,
				null);
		
		if(c.moveToLast()) {
			for(int i = 0; i < 10; i++) {
				locations.add(new GeoLocation(c.getInt(c.getColumnIndex("nid")),
								c.getInt(c.getColumnIndex("latitude")),
								c.getInt(c.getColumnIndex("longitude")),
								c.getString(c.getColumnIndex("title"))));
				if(!c.moveToPrevious()) {
					break;
				}
			}
		}
		
		c.close();
		DataReturn<List<GeoLocation>> result;
		GeoLocationsRetrieve glr = new GeoLocationsRetrieve(
				(latitudeA - 0.5) / 1000000, (longitudeA - 0.5) / 1000000,
				(latitudeB - 0.5) / 1000000, (longitudeB - 0.5) / 1000000);
		
		synchronized(this) {
			result = new DataReturn<List<GeoLocation>>(
												locations, activeObjects.size());
			activeObjects.add(result);
		}
		glr.addOperationListener(result);
		new NetworkTask<List<GeoLocation>>().execute(glr);
		return result;
	}
	
	/**
	 * Gets the ReferencePoint that is currently used as reference point.
	 * Unless setCurrentReferencePoint has been called earlier, the physical
	 * position of the phone is returned.
	 * 
	 * @return the current reference point, as a ReferencePoint object.
	 */
	public ReferencePoint getCurrentReferencePoint() {
		Log.i("getCurrent", "Value is " + firstRequest);
		if(firstRequest) {
			String[] idCol = {"_id"};
			Cursor c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
					idCol,
					"current = 1",
					null,
					null,
					null,
					null);
			if(c.moveToPosition(0)) {
				setCurrentReferencePoint(c.getInt(c.getColumnIndex("_id")));
				Log.i("Value of current: ", Integer.toString(c.getInt(c.getColumnIndex("_id"))));
			}
			c.close();
			firstRequest = false;
		}
				return currentReferencePoint == null ? null /* physical position here */ : currentReferencePoint;
	}
	
	/**
	 * Returns all reference points saved in the database, and the physical
	 * position of the phone, which is always a reference point.
	 * 
	 * @return all reference point, including the physical location of the phone.
	 */
	public List<ReferencePoint> getAllReferencePoints() {
		Cursor c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
				null,
				null,
				null,
				null,
				null,
				null);
		
		List<ReferencePoint> glList = new ArrayList<ReferencePoint>();
		if(c.moveToPosition(0)) {
			do {
				glList.add(new ReferencePoint(c.getInt(c.getColumnIndex("_id")),
						new GeoPoint(c.getInt(c.getColumnIndex("latitude")),
						c.getInt(c.getColumnIndex("longitude"))), 
						c.getString(c.getColumnIndex("name"))));
			} while(c.moveToNext());
		}
		c.close();
		
		return glList;
	}
	
	/**
	 * Sets the ReferencePoint to use as the current reference point.
	 * 
	 * @param id the id of the already existing reference point
	 */
	public void setCurrentReferencePoint(int id) {
		Cursor c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
				null,
				"_id = " + id,
				null,
				null,
				null,
				null);
		
		if(c.moveToPosition(0)) {
			currentReferencePoint = new ReferencePoint(c.getInt(c.getColumnIndex("_id")),
							   new GeoPoint(
									   c.getInt(c.getColumnIndex("latitude")),
									   c.getInt(c.getColumnIndex("longitude"))),
							   c.getString(c.getColumnIndex("name")));
		}
		c.close();
		resetCurrentRefPoint();
		ContentValues values = new ContentValues();
		values.put("current", 1);
		dbHelper.getWritableDatabase().update(DatabaseHelper.REFERENCE_POINT_TABLE,
				values,
				"_id = " + id,
				null);
	}
	
	private void resetCurrentRefPoint() {
		ContentValues values = new ContentValues();
		values.put("current", 0);
		dbHelper.getWritableDatabase().update(DatabaseHelper.REFERENCE_POINT_TABLE,
				values,
				"current = 1",
				null);
	}

	private void addReferencePoint(GeoPoint gp, String name, boolean isCurrent) {
		Log.i("W�nge", "New reference point added");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] args = {name, Integer.toString(gp.getLatitudeE6()),
										 Integer.toString(gp.getLongitudeE6())};
		Cursor c = db.query(DatabaseHelper.REFERENCE_POINT_TABLE,
				null,
				"name = ? AND latitude = ? AND longitude = ?",
				args,
				null,
				null,
				null);
		if(!c.moveToFirst()) {
			ContentValues values = new ContentValues();
			values.put("current", (isCurrent ? 1 : 0));
			values.put("name", name);
			values.put("latitude", gp.getLatitudeE6());
			values.put("longitude", gp.getLongitudeE6());
			db.insert(DatabaseHelper.REFERENCE_POINT_TABLE,
					null,
					values);
		}
		c.close();
	}
	/**
	 * Adds a reference point to the database.
	 * @param b 
	 * 
	 * @param rp the point to add
	 */
	public void addReferencePoint(GeoPoint gp, String name) {		
		addReferencePoint(gp, name, false);
	}
	
	/**
	 * Removes a reference point from the database.
	 * 
	 * @param gl the point to remove
	 */
	public void removeReferencePoint(int id) {
		dbHelper.getWritableDatabase().delete(DatabaseHelper.REFERENCE_POINT_TABLE,
				"_id = " + id,
				null);
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
		ContentValues values = new ContentValues();
		values.put("nid", nid);
		values.put("comment", commentText);
		values.put("author", author);
		values.put("title", title);
		values.put("added_date", "N/A");
		
		dbHelper.getWritableDatabase().insert(DatabaseHelper.COMMENT_TABLE,
				null,
				values);
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
		ContentValues values = new ContentValues();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		values.put("nid", a.getId());
		values.put("body", a.getBody());
		values.put("author", a.getAuthor());
		db.insert(DatabaseHelper.ANNOTATION_TABLE,
				null,
				values);
		
		values.clear();
		values.put("nid", a.getId());
		values.put("latitude", a.getGeoLocation().getLocation().getLatitudeE6());
		values.put("longitude", a.getGeoLocation().getLocation().getLongitudeE6());
		values.put("title", a.getGeoLocation().getTitle());
		db.insert(DatabaseHelper.GEOLOCATION_TABLE,
				null,
				values);
		
		for(Comment c : a.getComments()) {
			values.clear();
			values.put("nid", a.getId());
			values.put("comment", c.getCommentText());
			values.put("author", c.getAuthor());
			values.put("title", c.getTitle());
			values.put("added_date", c.getAddedDate().toString());
			db.insert(DatabaseHelper.COMMENT_TABLE,
					null,
					values);
		}
		return true;
	}

	//Inserts the given list with GeoLocations into its table.
	//If error occurs, false is returned.
	private boolean insertData(List<GeoLocation> glList) {
		ContentValues values = new ContentValues();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		for(GeoLocation gl : glList) {
			values.clear();
			values.put("nid", gl.getId());
			values.put("latitude", gl.getLocation().getLatitudeE6());
			values.put("longitude", gl.getLocation().getLongitudeE6());
			values.put("title", gl.getTitle());
			db.insert(DatabaseHelper.GEOLOCATION_TABLE,
					null,
					values);
		}
		return true;
	}

	private List<DataReturn<?>> activeObjects = new ArrayList<DataReturn<?>>();
	private ReferencePoint currentReferencePoint;
	private boolean firstRequest = true;

	public void operationExcecuted(OperationResult<Annotation> result) {
		if(!result.hasErrors()) {
			insertData(result.getResult());
		}
	}

	public void onLocationChanged(Location location) {
		if(!dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
				null,
				"name = physical_position",
				null,
				null,
				null,
				null).moveToPosition(0)) {
			ContentValues values = new ContentValues();
			values.put("name", "physical_location");
			values.put("current", 1);
			dbHelper.getReadableDatabase().insert(DatabaseHelper.REFERENCE_POINT_TABLE,
					null,
					values);
		}
		
		ContentValues values = new ContentValues();
		Log.i("Physical locations: ", location.getLatitude() + " " + location.getLongitude());
		values.put("latitude", location.getLatitude());
		values.put("longitude", location.getLongitude());
		dbHelper.getWritableDatabase().update(DatabaseHelper.REFERENCE_POINT_TABLE,
				values,
				"name = physical_position",
				null);
	}
	public void onProviderDisabled(String provider) {}
	public void onProviderEnabled(String provider) {}
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}
