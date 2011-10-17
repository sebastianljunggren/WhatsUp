package nu.placebo.whatsup.datahandling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.Comment;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.NetworkTask;
import android.database.Cursor;

/**
 * Handles connection with the database.
 * 
 * @author Wånge
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
		//Calling local cache first, then putting a network call in the queue, returning an object containing the content of the local
		//cache, and means of acquiring the data fetched from remote server
		boolean exists = true;
			
		Cursor c = dbHelper.getReadableDatabase().query(
				DatabaseHelper.ANNOTATION_TABLE, 
				null, 
				"nid = " + nid, 
				null, 
				null, 
				null, 
				null);
		
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
				null, 
				null, 
				null, 
				null);
		
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
				null, 
				null, 
				null, 
				null, 
				null, 
				null);
		
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
	 * Returns a list of GeoLocations 
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
				null,
				null,
				null);
		
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
}
