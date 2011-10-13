package nu.placebo.whatsuptest.modeltest;

import nu.placebo.whatsup.model.SessionHandler;
import nu.placebo.whatsup.model.SessionInfo;
import android.test.AndroidTestCase;
/**
 * 
 * Tests SessionHandler.
 *
 */
public class SessionHandlerTest extends AndroidTestCase {
	
	public void setUp() throws Exception {

		super.setUp();
	}
	
	public void testSaveSession() {
			SessionHandler handler = SessionHandler.getInstance(this.getContext());
			SessionInfo si = new SessionInfo("Name", "Id");
			handler.saveSession(si);
			assertTrue("Session saved not equal to session returned", si.equals(handler.getSession()));
			assertTrue("Handler should have session",handler.hasSession());
			handler.saveSession(new SessionInfo(null, null));
			assertFalse("SH has session after it was cleared", handler.hasSession());
	}

}
