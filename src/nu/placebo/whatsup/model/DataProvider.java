package nu.placebo.whatsup.model;

import java.io.IOException;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
	
public class DataProvider {
	
	/**
	 * Class is Singelton, disallow creation
	 */
	private DataProvider() {}
	
	private static final DataProvider instance = new DataProvider();
	
	private DatabaseHelper dbHelper = new DatabaseHelper(null);
	
	public static DataProvider getDataProvider() {
		return instance;
	}
	
	public Annotation getAnnotation(int nid) {
		//Calling local cache first, then putting a network call in the queue, returning an object containing the content of the local
		//cache, and means of acquiring the data fetched from remote server
		return null;
	}
	
	public List<GeoLocation> getAnnotationMarkers(double latitudeA,
			double longitudeA, double latitudeB, double longitudeB) {
		//Same idea as above
		return null;
	}
}
