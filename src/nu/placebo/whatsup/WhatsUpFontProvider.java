package nu.placebo.whatsup;

import android.graphics.Typeface;

import com.ericsson.mmaps.FontProvider;
import com.ericsson.mmaps.graphics.RaveGeoFont;

public class WhatsUpFontProvider implements FontProvider {

	public RaveGeoFont medium() {
		return new RaveGeoFont(Typeface.DEFAULT, 14);
	}

	public RaveGeoFont mediumBold() {
		return new RaveGeoFont(Typeface.DEFAULT_BOLD, 14);
	}

	public RaveGeoFont small() {
		return new RaveGeoFont(Typeface.DEFAULT, 12);
	}

	public RaveGeoFont smallBold() {
		return new RaveGeoFont(Typeface.DEFAULT_BOLD, 12);
	}

	public RaveGeoFont smallItalic() {
		return new RaveGeoFont(Typeface.create(Typeface.DEFAULT,
				Typeface.ITALIC), 12);
	}
}
