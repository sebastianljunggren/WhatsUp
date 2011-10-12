package nu.placebo.whatsup.network;

import android.os.AsyncTask;

public class NetworkTask<T> extends AsyncTask<NetworkOperation<T>, Void, NetworkOperation<T>> {

	@Override
	protected NetworkOperation<T> doInBackground(NetworkOperation<T>... operations) {
		for(NetworkOperation<T> o: operations) {
			o.setOperationResult(o.execute());
			return o;
		}
		return null;
	}

	@Override
	protected void onPostExecute(NetworkOperation<T> o) {
		o.notifyListeners(o.getResult());
	}

}
