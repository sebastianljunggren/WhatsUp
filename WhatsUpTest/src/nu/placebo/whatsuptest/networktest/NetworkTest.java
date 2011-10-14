package nu.placebo.whatsuptest.networktest;

import java.util.List;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.model.GeoLocation;
import nu.placebo.whatsup.model.SessionInfo;
import nu.placebo.whatsup.network.AnnotationCreate;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.GeoLocationsRetrieve;
import nu.placebo.whatsup.network.Login;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.OperationResult;
import nu.placebo.whatsup.network.SessionTest;
import android.test.AndroidTestCase;

import com.google.android.maps.GeoPoint;

/**
 * 
 * Test the network calls
 * 
 */
public class NetworkTest extends AndroidTestCase {

	public NetworkTest() {
		super();
	}

	public void setUp() throws Exception {

		super.setUp();
	}

	public void testAnnotationRetrieveFail() {
		AnnotationRetrieve a = new AnnotationRetrieve(0);
		assertTrue(true);
		a.addOperationListener(new NetworkOperationListener<Annotation>() {

			@Override
			public void operationExcecuted(OperationResult<Annotation> result) {
				assertTrue(
						"No errors when retrieving annotation with nonexistant id",
						result.hasErrors());
				assertNull("Annotation returned was not null",
						result.getResult());
				assertTrue("Error code not set, ",
						result.getStatusCode() >= 300);
			}

		});
		OperationResult<Annotation> result = a.execute();
		a.notifyListeners(result);
	}

	public void testAnnotationRetrieve() {
		AnnotationRetrieve a = new AnnotationRetrieve(1337);
		assertTrue(true);
		a.addOperationListener(new NetworkOperationListener<Annotation>() {

			@Override
			public void operationExcecuted(OperationResult<Annotation> result) {
				assertFalse("Errors when retrieving annotation",
						result.hasErrors());
				assertNotNull("Annotation returned was null",
						result.getResult());
				assertEquals("Incorrect nid on the Annotation returned.", 1337,
						result.getResult().getId());
			}

		});
		OperationResult<Annotation> result = a.execute();
		a.notifyListeners(result);
	}

	public void testSession() {
		Login l = new Login("test", "WhatsUp!");
		l.addOperationListener(new NetworkOperationListener<SessionInfo>() {

			@Override
			public void operationExcecuted(OperationResult<SessionInfo> result) {
				assertFalse("Errors when loggin in", result.hasErrors());
				assertNotNull("SessionInfo returned was null",
						result.getResult());
			}

		});
		OperationResult<SessionInfo> result = l.execute();
		SessionInfo session = result.getResult();
		l.notifyListeners(result);

		SessionTest st = new SessionTest(session);
		st.addOperationListener(new NetworkOperationListener<SessionInfo>() {

			@Override
			public void operationExcecuted(OperationResult<SessionInfo> result) {
				assertFalse("Errors when testing session from log in.",
						result.hasErrors());
				assertNotNull("SessionInfo returned was null",
						result.getResult());
			}

		});
		st.setOperationResult(st.execute());
		st.notifyListeners(st.getResult());
	}

	public void testSessionFail() {
		Login l = new Login("test", "WrongPass!");
		l.addOperationListener(new NetworkOperationListener<SessionInfo>() {

			@Override
			public void operationExcecuted(OperationResult<SessionInfo> result) {
				assertTrue(
						"Errors not set when logging in with wrong credentials.",
						result.hasErrors());
				assertNull(
						"SessionInfo returned even though there were errors",
						result.getResult());
			}

		});
		OperationResult<SessionInfo> result = l.execute();
		SessionInfo session = result.getResult();
		l.notifyListeners(result);

		SessionTest st = new SessionTest(session);
		st.addOperationListener(new NetworkOperationListener<SessionInfo>() {

			@Override
			public void operationExcecuted(OperationResult<SessionInfo> result) {
				assertTrue("Errors not set when testing faulty SessionInfo.",
						result.hasErrors());
				assertNull(
						"SessionInfo returned was not null when testing faulty session.",
						result.getResult());
			}

		});
		st.setOperationResult(st.execute());
		st.notifyListeners(st.getResult());
	}

	public void testGeoLocationRetrieve() {
		GeoLocationsRetrieve gr = new GeoLocationsRetrieve(-90, -90, 90, 90);
		assertTrue(true);
		gr.addOperationListener(new NetworkOperationListener<List<GeoLocation>>() {

			@Override
			public void operationExcecuted(
					OperationResult<List<GeoLocation>> result) {
				assertFalse("Errors when retrieving GeoLocations",
						result.hasErrors());
				assertNotNull("List of GeoLocations returned was null",
						result.getResult());
				assertNotSame("Empty list returned", result.getResult().size(),
						0);
			}

		});
		OperationResult<List<GeoLocation>> result = gr.execute();
		gr.notifyListeners(result);
	}

	public void testGeoLocationRetrieveFail() {
		GeoLocationsRetrieve gr = new GeoLocationsRetrieve(360, 360, 360, 360);
		gr.addOperationListener(new NetworkOperationListener<List<GeoLocation>>() {

			@Override
			public void operationExcecuted(
					OperationResult<List<GeoLocation>> result) {
				assertFalse("Errors when retrieving GeoLocations",
						result.hasErrors());
				assertEquals("List of GeoLocations not empty.", result
						.getResult().size(), 0);
			}

		});
		OperationResult<List<GeoLocation>> result = gr.execute();
		gr.notifyListeners(result);
	}

	public void testAnnotationCreate() {
		Login l = new Login("test", "WhatsUp!");
		SessionInfo sessionInfo = l.execute().getResult();
		AnnotationCreate ac = new AnnotationCreate("Test", "Test body", "test",
				new GeoPoint(0, 0), sessionInfo);
		assertTrue(true);
		ac.addOperationListener(new NetworkOperationListener<Annotation>() {

			@Override
			public void operationExcecuted(OperationResult<Annotation> result) {
				assertFalse("Errors when creating annotation",
						result.hasErrors());
				assertNotNull("No annotation returned.", result.getResult());
			}
		});
		OperationResult<Annotation> result = ac.execute();
		ac.notifyListeners(result);
	}
	public void testAnnotationCreateFail() {
		Login l = new Login("test", "BadPass!");
		SessionInfo sessionInfo = l.execute().getResult();
		AnnotationCreate ac = new AnnotationCreate("Test", "Test body", "test",
				new GeoPoint(0, 0), sessionInfo);
		assertTrue(true);
		ac.addOperationListener(new NetworkOperationListener<Annotation>() {

			@Override
			public void operationExcecuted(OperationResult<Annotation> result) {
				assertFalse("No errors when creating annotation",
						result.hasErrors());
				assertNull("Annotation returned.", result.getResult());
			}
		});
		OperationResult<Annotation> result = ac.execute();
		ac.notifyListeners(result);
	}
}
