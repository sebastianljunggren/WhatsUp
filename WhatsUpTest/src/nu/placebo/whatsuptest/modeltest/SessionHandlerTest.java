package nu.placebo.whatsuptest.modeltest;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
			handler.reset();
			SessionInfo si = new SessionInfo("Name", "Id");
			handler.saveSession(si);
			assertTrue("Session saved not equal to session returned", si.equals(handler.getSession()));
			assertTrue("Handler should have session",handler.hasSession());
			handler.saveSession(new SessionInfo(null, null));
			assertFalse("SH has session after it was cleared", handler.hasSession());
	}
	
	public void testLogInOut() throws InterruptedException {
		final SessionHandler handler = SessionHandler.getInstance(this.getContext());
		handler.reset();
		final CountDownLatch signal = new CountDownLatch(1);
		handler.saveCredentials("Test", "WhatsUp!");
		
		handler.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				assertTrue("Session not set", handler.hasSession());
				signal.countDown();
			}
		});
		
		handler.login();
		signal.await(5, TimeUnit.SECONDS);
		handler.logOut();
		assertFalse("Session not removed when logging out", handler.hasSession());
	}
}