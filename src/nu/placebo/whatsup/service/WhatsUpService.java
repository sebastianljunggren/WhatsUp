package nu.placebo.whatsup.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The service that runs network calls in the background.
 */

public class WhatsUpService extends Service {
	private WhatsUpBinder binder = new WhatsUpBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		return this.binder;
	}

}
