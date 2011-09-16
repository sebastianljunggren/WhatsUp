package nu.placebo.whatsup;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * The main activity of WhatsUp. Currently it just displays a dummy text.
 * 
 */

public class WhatsUpActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}