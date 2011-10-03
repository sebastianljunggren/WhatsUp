package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WhatsUpService extends Service {
	private WhatsUpBinder binder = new WhatsUpBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		return this.binder;
	}

}
