package nu.placebo.whatsuptest.networktest;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.SessionInfo;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.Login;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.OperationResult;
import nu.placebo.whatsup.network.SessionTest;
import android.test.AndroidTestCase;

public class NetworkTest extends AndroidTestCase {

	private SessionInfo session;

	public NetworkTest(){
		super();
	}
	
	public void setUp() throws Exception{
		
		super.setUp();
	}
	
	public void testAnnotationRetrieve(){
		AnnotationRetrieve a = new AnnotationRetrieve(1234);
		assertTrue(true);
		a.addOperationListener(new NetworkOperationListener<Annotation>() {

			@Override
			public void operationExcecuted(OperationResult<Annotation> result) {
				assertFalse("Errors when retrieving annotation", result.hasErrors());
				assertNotNull("Annotation reurned was null", result.getResult());
				assertEquals("Incorrect nid on the Annotation returned.", 1234, result.getResult().getId());
			}
			
		});
		OperationResult<Annotation> result = a.execute();
		a.notifyListeners(result);
	}
	
	public void testLogIn() {
		Login l = new Login("test", "WhatsUp!");
		l.addOperationListener(new NetworkOperationListener<SessionInfo>() {

			@Override
			public void operationExcecuted(OperationResult<SessionInfo> result) {
				assertFalse("Errors when loggin in", result.hasErrors());
				assertNotNull("SessionInfo returned was null",
						 session = result.getResult());
			}

		});
		OperationResult<SessionInfo> result = l.execute();
		this.session  = result.getResult();
		this.session.getSessionId();
		l.notifyListeners(result);
	}
	
	public void testSessionTest(){
		SessionTest st = new SessionTest(session);
		st.addOperationListener(new NetworkOperationListener<SessionInfo>() {

			@Override
			public void operationExcecuted(OperationResult<SessionInfo> result) {
				assertFalse("Errors when testing session from log in.", result.hasErrors());
				assertNotNull("SessionInfo returned was null",
						 session = result.getResult());
			}

		});
		st.setOperationResult(st.execute());
		st.notifyListeners(st.getResult());
	}
}
