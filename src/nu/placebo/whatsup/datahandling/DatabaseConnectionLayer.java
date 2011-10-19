package nu.placebo.whatsup.datahandling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.Comment;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.ReferencePoint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.maps.GeoPoint;

/**
 * Handles connection with the database.
 */
public class DatabaseConnectionLayer {

	private static DatabaseHelper dbHelper;
	
	static void setDatabaseHelper(DatabaseHelper dbh) {
		dbHelper = dbh;
	}
	
	/**
	 * Returns the Annotation with the given nid from the database, or null if it does not exist
	 * in the database.
	 * 
	 * @param nid the node id for the annotation.
	 * @return the annotation, as represented in the database, or null, if it does not exist in the database.
	 */
	static Annotation getAnnotation(int nid) {
		boolean exists = true;
			
		Cursor c = dbHelper.getReadableDatabase().query(
				DatabaseHelper.ANNOTATION_TABLE, 
				null, 
				"nid = " + nid, 
				null,null,null,null);
		
		String author = "";
		String body = "";
		if(c.moveToPosition(0)) {
			author = c.getString(c.getColumnIndex("author"));
			body = c.getString(c.getColumnIndex("body"));
		} else {
			exists = false;
		}
		
		c = dbHelper.getReadableDatabase().query(
				DatabaseHelper.GEOLOCATION_TABLE, 
				null, 
				"nid = " + nid, 
				null,null,null,null);
		
		int latitude = 0;
		int longitude = 0;
		String title = "";
		if(c.moveToPosition(0)) {
			latitude = c.getInt(c.getColumnIndex("latitude"));
			longitude = c.getInt(c.getColumnIndex("longitude"));
			title = c.getString(c.getColumnIndex("title"));
		} else {
			exists = false;
		}
		
		c = dbHelper.getReadableDatabase().query(
				DatabaseHelper.COMMENT_TABLE, 
				null,null,null,null,null,null);
		
		List<Comment> comments = new ArrayList<Comment>();
		if(c.moveToPosition(0)) {
			do {
				String comment = c.getString(c.getColumnIndex("comment"));
				String cTitle = c.getString(c.getColumnIndex("title"));
				String cAuthor = c.getString(c.getColumnIndex("author"));
				Date cDate = new Date(c.getLong(c.getColumnIndex("added_date")));
				comments.add(new Comment(cAuthor, comment, cTitle, cDate));
			} while(!c.isLast());
		} else {
			exists = false;
		}
		c.close();
		
		synchronized(dbHelper) {
			return (exists ? new Annotation(new GeoLocation(nid, latitude, longitude, title), 
									body, author, comments) : null);
		}
	}
	
	/**
	 * Returns a list of the GeoLocations within the area given by applying the parameters 
	 * constraints.
	 * 
	 * @param maxLat
	 * @param maxLong
	 * @param minLat
	 * @param minLong
	 * @return
	 */
	static List<GeoLocation> getAnnotationMarkers(int maxLat, 
			int maxLong, int minLat, int minLong) {
		
		String[] selectionArgs = {Integer.toString(maxLat), Integer.toString(maxLong),
								  Integer.toString(minLat), Integer.toString(minLong)};
		
		Cursor c = dbHelper.getReadableDatabase().query(DatabaseHelper.GEOLOCATION_TABLE,
				null,
				"latitude < ? AND longitude < ? AND latitude > ? AND longitude > ?",
				selectionArgs,
				null,null,null);
		
		List<GeoLocation> locations = new ArrayList<GeoLocation>();
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
		return locations;
	}
	
	/**
	 * Returns all reference points.
	 * 
	 * @return
	 */
	static List<ReferencePoint> getAllReferencePoints() {
		Cursor c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
				null,null,null,null,null,
				"current DESC");
		
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
	 * Sets the reference point with the specified id as the current reference point.
	 * The last current reference point is also cleared. Sending a negative id will set the physical location as
	 * current location (calling with the id of the physical location also works).
	 * 
	 * @param id the id of the reference point that is desired as the current reference point.
	 * @return the new current reference point if it existed in the database, and the old one
	 * if not.
	 */
	static ReferencePoint setCurrentReferencePoint(int id) {
		Cursor c;
		if(id < 0) {
			c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
					null,
					"name = 'physical_position'",
					null,null,null,null);
		} else {
			c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
					null,
					"_id = " + id,
					null,null,null,null);
		}
		if(c.moveToFirst()) {
			resetCurrentRefPoint();
			ContentValues values = new ContentValues();
			values.put("current", 1);
			if(id < 0) {
				dbHelper.getWritableDatabase().update(DatabaseHelper.REFERENCE_POINT_TABLE,
						values,
						"name = 'physical_position'",
						null);
			} else {
				dbHelper.getWritableDatabase().update(DatabaseHelper.REFERENCE_POINT_TABLE,
						values,
						"_id = " + id,
						null);
			}
		}
		c.close();
		c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
				null,
				"current = 1",
				null,null,null,null);
		c.moveToFirst();
		ReferencePoint result = new ReferencePoint(c.getInt(c.getColumnIndex("_id")),
						new GeoPoint(
								c.getInt(c.getColumnIndex("latitude")),
								c.getInt(c.getColumnIndex("longitude"))), 
			c.getString(c.getColumnIndex("name")));
		c.close();
		return result;
	}

	private static void resetCurrentRefPoint() {
		ContentValues values = new ContentValues();
		values.put("current", 0);
		dbHelper.getWritableDatabase().update(DatabaseHelper.REFERENCE_POINT_TABLE,
				values,
				"current = 1",
				null);
	}
	
	/**
	 * Adds a reference point to the database. If the point already exists, nothing is done.
	 * 
	 * @param gp the geographical location of the reference point.
	 * @param name the name of the reference point.
	 */
	static void addReferencePoint(GeoPoint gp, String name) {
		String whereName = "name = '"+name+"'";
		Cursor c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
				null,
				whereName,
				null,null,null,null);
		if(!c.moveToFirst()) {
			ContentValues values = new ContentValues();
			values.put("current", 0);
			values.put("name", name);
			values.put("latitude", gp.getLatitudeE6());
			values.put("longitude", gp.getLongitudeE6());
			dbHelper.getWritableDatabase().insert(DatabaseHelper.REFERENCE_POINT_TABLE,
					null,
					values);
		}
		c.close();
	}
	
	/**
	 * 
	 * @param gp
	 */
	static void updateCurrentLocation(GeoPoint gp) {
		ContentValues values = new ContentValues();
		values.put("latitude", gp.getLatitudeE6());
		values.put("longitude", gp.getLongitudeE6());
		dbHelper.getWritableDatabase().update(DatabaseHelper.REFERENCE_POINT_TABLE,
				values,
				"name = 'physical_location'",
				null);
	}
	
	/**
	 * Removes a reference point from the database.
	 * 
	 * @param id the id of the point to remove. If this point does not exist, nothing is done.
	 */
	static void removeReferencePoint(int id) {
		dbHelper.getWritableDatabase().delete(DatabaseHelper.REFERENCE_POINT_TABLE,
				"_id = " + id,
				null);
		Cursor c = dbHelper.getReadableDatabase().query(DatabaseHelper.REFERENCE_POINT_TABLE,
				null,
				"current = 1",
				null,null,null,null);
		if(!c.moveToFirst()) {
			DataProvider.getDataProvider(null).setCurrentReferencePoint(-1);
			c.close();
		}
	}
	
	/**
	 * 
	 * @param nid
	 * @param author
	 * @param commentText
	 * @param title
	 */
	static void storeComment(int nid, String author, String commentText, String title) {
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
	 * 
	 * @param a
	 * @return
	 */
	static boolean storeAnnotation(Annotation a) {
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
	
	/**
	 * 
	 * @param glList
	 * @return
	 */
	static boolean storeGeoLocations(List<GeoLocation> glList) {
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
}
