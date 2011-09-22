package nu.placebo.whatsup;

import java.io.IOException;

import android.content.res.Resources;

import com.ericsson.mmaps.SymbolProvider;
import com.ericsson.mmaps.graphics.RaveGeoImage;

public class WhatsUpSymbolProvider implements SymbolProvider {

    private RaveGeoImage airport = null;
	private RaveGeoImage subway = null;
	private RaveGeoImage railway = null;
	private RaveGeoImage ferry = null;
	private RaveGeoImage poi = null;
	private RaveGeoImage city = null;
	private RaveGeoImage bus = null;

	public boolean hasSymbols() {
        return false;
    }

    public RaveGeoImage airport() throws IOException {
    	if(airport == null) {
    		airport = RaveGeoImage.createImage(Resources.getSystem().getDrawable(R.drawable.icon));	
    	}
        return airport;
    }

    public RaveGeoImage bus() throws IOException {
    	if(bus == null) {
    		bus = RaveGeoImage.createImage(Resources.getSystem().getDrawable(R.drawable.icon));
    	}
    	return bus;
    }

    public RaveGeoImage city() throws IOException {
    	if(city == null) {
    		city = RaveGeoImage.createImage(Resources.getSystem().getDrawable(R.drawable.icon));
    	}
    	return city;
    }

    public RaveGeoImage defaultPoi() throws IOException {
    	if(poi == null) {
    		poi = RaveGeoImage.createImage(Resources.getSystem().getDrawable(R.drawable.icon));
    	}
    	return poi;
    }

    public RaveGeoImage ferry() throws IOException {
    	if(ferry == null) {
    		ferry = RaveGeoImage.createImage(Resources.getSystem().getDrawable(R.drawable.icon));
    	}
    	return ferry;
    }

    public RaveGeoImage railway() throws IOException {
    	if(railway == null) {
    		railway = RaveGeoImage.createImage(Resources.getSystem().getDrawable(R.drawable.icon));
    	}
    	return railway;
    }

    public RaveGeoImage subway() throws IOException {
    	if(subway == null) {
    		subway = RaveGeoImage.createImage(Resources.getSystem().getDrawable(R.drawable.icon));	
    	}
        return subway;
    }

}
