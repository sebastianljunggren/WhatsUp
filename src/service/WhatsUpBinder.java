package service;

import nu.placebo.whatsup.model.DataProvider;
import android.os.Binder;

public class WhatsUpBinder extends Binder {
	private DataProvider dataProvider = DataProvider.getDataProvider();

	public void retrieveAnnotation(int nid) {
		this.dataProvider.retrieveAnnotation(nid);

	}

	public void retrieveGeolocations(double latitudeA, double longitudeA,
			double latitudeB, double longitudeB) {
		this.dataProvider.retrieveGeoLocations(latitudeA, longitudeA,
				latitudeB, longitudeB);
	}
}
