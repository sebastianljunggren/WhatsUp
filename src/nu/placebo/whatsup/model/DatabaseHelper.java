package nu.placebo.whatsup.model;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	//-------------------- Constants -----------------
	private static final String DATABASE_NAME = "whatsup.db";
	private static final int DATABASE_VERSION = 2;
	private static final String ANNOTATION_MARKER_TABLE = "annotation_markers";
	//------------------------------------------------

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + ANNOTATION_MARKER_TABLE + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nid INTEGER,"
				+ "latitude REAL,"
				+ "longitude REAL,"
				+ "title TEXT,"
				+ "vote INTEGER"
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ANNOTATION_MARKER_TABLE);
		onCreate(db);
	}
	
	public List<GeoLocation> retrieveAnnotationMarkers(double latitudeA,
		double longitudeA, double latitudeB, double longitudeB) {
		/* UNDER CONSTRUCTION
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(ANNOTATION_MARKER_TABLE, null, "", selectionArgs, groupBy, having, orderBy);
		*/
		return null;
	}
	
	public Annotation retrieveAnnotation(int nid) {
		/* UNDER CONSTRUCTION
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(ANNOTATION_MARKER_TABLE, null, "nid = " + nid, null, null, null, null);
		c.
		*/
		return null;
	}
}
