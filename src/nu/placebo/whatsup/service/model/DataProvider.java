package nu.placebo.whatsup.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.Comment;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.NetworkQueue;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataProvider {

	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		//-------------------- Constants -----------------
		private static final String DATABASE_NAME = "whatsup.db";
		private static final int DATABASE_VERSION = 2;
		private static final String GEOLOCATION_TABLE = "geolocations";
		private static final String ANNOTATION_TABLE = "anntations";
		private static final String COMMENT_TABLE = "comments";
		private static final String REFERENCE_POINT_TABLE = "reference_points";
		//------------------------------------------------

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + GEOLOCATION_TABLE + " ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "nid INTEGER,"
					+ "latitude REAL,"
					+ "longitude REAL,"
					+ "title TEXT"
					+ ");");
			db.execSQL("CREATE TABLE " + ANNOTATION_TABLE + " ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "nid INTEGER,"
					+ "body TEXT,"
					+ "author TEXT"
					+ ");");
			db.execSQL("CREATE TABLE " + COMMENT_TABLE + " ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT"
					+ "nid REAL," 
					+ "comment TEXT,"
					+ "author TEXT,"
					+ "title TEXT,"
					+ "added_date TEXT"
					+ ");");
			db.execSQL("CREATE TABLE " + REFERENCE_POINT_TABLE + " ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT"
					+ "name TEXT,"
					+ "latitude REAL,"
					+ "longitude REAL"
					+ ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + GEOLOCATION_TABLE);
			onCreate(db);
		}
	}
	
	/**
	 * Class is Singelton, disallow outside creation
	 */
	private DataProvider(Context c) {
		dbHelper = new DatabaseHelper(c);
	}

	private static DataProvider instance;

	private DatabaseHelper dbHelper;
	private NetworkQueue networkQueue = NetworkQueue.getInstance();

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
		
		c.moveToFirst();
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
		
		c.moveToFirst();
		int latitude = c.getInt(c.getColumnIndex("latitude"));
		int longitude = c.getInt(c.getColumnIndex("longitude"));
		String title = c.getString(c.getColumnIndex("title"));
		
		c = dbHelper.getReadableDatabase().query(
				DatabaseHelper.COMMENT_TABLE, 
				null, 
				"", 
				null, 
				null, 
				null, 
				null);
		
		c.moveToFirst();
		List<Comment> comments = new ArrayList<Comment>();
		do {
			String comment = c.getString(c.getColumnIndex("comment"));
			String cTitle = c.getString(c.getColumnIndex("title"));
			String cAuthor = c.getString(c.getColumnIndex("author"));
			Date cDate = new Date(c.getLong(c.getColumnIndex("added_date")));
			comments.add(new Comment(cAuthor, comment, cTitle, cDate));
		} while(!c.isLast());
		
		DataReturn<Annotation> result;
		
		synchronized(this) {
			result = new DataReturn<Annotation>(new Annotation(
				new GeoLocation(nid, latitude, longitude, title), 
				body, author, comments), activeObjects.size());
		}
		activeObjects.add(result);
		AnnotationRetrieve ar = new AnnotationRetrieve(nid);
		ar.addOperationListener(result);
		networkQueue.add(ar);
		return result;
	}
	
	public DataReturn<List<GeoLocation>> getAnnotationMarkers(double latitudeA,
			double longitudeA, double latitudeB, double longitudeB) {
		//Database part here
		List<GeoLocation> locations = new ArrayList<GeoLocation>();
		String maxLat = Double.toHexString(Math.max(latitudeA, latitudeB));
		String maxLong = Double.toHexString(Math.max(longitudeA, longitudeB));
		String minLat = Double.toHexString(Math.min(latitudeA, latitudeB));
		String minLong = Double.toHexString(Math.min(longitudeA, longitudeB));
		String[] selectionArgs = {maxLat, maxLong, minLat, minLong};
		
		dbHelper.getReadableDatabase().query(DatabaseHelper.ANNOTATION_TABLE,
				null,
				"latitude < ? AND longitude < ? AND latitude > ? AND longitude > ?",
				selectionArgs,
				null,
				null,
				null);
		
		DataReturn<List<GeoLocation>> result;
		GeoLocationsRetrieve glr = new GeoLocationsRetrieve(
				latitudeA, longitudeA, latitudeB, longitudeB);
		
		synchronized(this) {
			result = new DataReturn<List<GeoLocation>>(
												locations, activeObjects.size());
			activeObjects.add(result);
		}
		glr.addOperationListener(result);
		networkQueue.add(glr);
		return result;
	}
	
	/**
	 * Called by DataReturn object to notify the DataProvider about new data
	 * to insert into the local database.
	 * 
	 * @param newData whether the data differs from that in the local database
	 * @param id 
	 */
	@SuppressWarnings("unchecked")
	void newDataRecieved(boolean newData, int id) {
		if(newData) {
			Object data = activeObjects.get(id).getNewData();
			
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
		activeObjects.remove(id);
	}
	
	private boolean insertData(Annotation a) {
		ContentValues values = new ContentValues();
		
		values.put("nid", a.getId());
		values.put("body", a.getBody());
		values.put("author", a.getAuthor());
		dbHelper.getWritableDatabase().insert(DatabaseHelper.ANNOTATION_TABLE,
				null,
				values);
		
		values.clear();
		values.put("nid", a.getId());
		values.put("latitude", a.getGeoLocation().getLocation().getLatitudeE6());
		values.put("longitude", a.getGeoLocation().getLocation().getLongitudeE6());
		values.put("title", a.getGeoLocation().getTitle());
		return false;
	}

	private boolean insertData(List<GeoLocation> glList) {
		return false;
	}

	private List<DataReturn<?>> activeObjects = new ArrayList<DataReturn<?>>();
}
