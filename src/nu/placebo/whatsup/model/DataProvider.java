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
	private DataProvider() {
	}

	private static final DataProvider instance = new DataProvider();

	private DatabaseHelper dbHelper = new DatabaseHelper(null);

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
		// TODO Auto-generated method stub

	}

	public void retrieveGeoLocations(double latitudeA, double longitudeA,
			double latitudeB, double longitudeB) {
		// Fetches GeoLocations from the server and stores them in the local
		// database.
		// TODO Auto-generated method stub

	}
}
