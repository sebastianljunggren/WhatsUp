package nu.placebo.whatsup.datahandling;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
		
	//-------------------- Constants -----------------
	static final String DATABASE_NAME = "whatsup.db";
	static final int DATABASE_VERSION = 2;
	static final String GEOLOCATION_TABLE = "geolocations";
	static final String ANNOTATION_TABLE = "anntations";
	static final String COMMENT_TABLE = "comments";
	static final String REFERENCE_POINT_TABLE = "reference_points";
	//------------------------------------------------

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + GEOLOCATION_TABLE + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nid INTEGER,"
				+ "latitude INTEGER,"
				+ "longitude INTEGER,"
				+ "title TEXT"
				+ ");");
		db.execSQL("CREATE TABLE " + ANNOTATION_TABLE + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nid INTEGER,"
				+ "body TEXT,"
				+ "author TEXT"
				+ ");");
		db.execSQL("CREATE TABLE " + COMMENT_TABLE + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nid REAL," 
				+ "comment TEXT,"
				+ "author TEXT,"
				+ "title TEXT,"
				+ "added_date TEXT"
				+ ");");
		db.execSQL("CREATE TABLE " + REFERENCE_POINT_TABLE + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name TEXT,"
				+ "latitude INTEGER,"
				+ "longitude INTEGER,"
				+ "current INTEGER"
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + GEOLOCATION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ANNOTATION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + COMMENT_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + REFERENCE_POINT_TABLE);
		onCreate(db);
	}
}
