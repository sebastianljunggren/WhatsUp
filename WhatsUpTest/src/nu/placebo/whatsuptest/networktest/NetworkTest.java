package nu.placebo.whatsuptest.networktest;

import nu.placebo.whatsup.model.Annotation;
import nu.placebo.whatsup.network.AnnotationRetrieve;
import nu.placebo.whatsup.network.NetworkOperationListener;
import nu.placebo.whatsup.network.NetworkQueue;
import nu.placebo.whatsup.network.OperationResult;
import android.test.AndroidTestCase;

public class NetworkTest extends AndroidTestCase {
	
	private NetworkQueue networkQueue;



	public NetworkTest(){
		super();
		this.networkQueue = NetworkQueue.getInstance();
	}
	
	public void setUp() throws Exception{
		
		super.setUp();
	}
	
	public void testAnnotationRetrieve(){
		AnnotationRetrieve a = new AnnotationRetrieve(1234);
		a.addOperationListener(new NetworkOperationListener<Annotation>() {

			@Override
			public void operationExcecuted(OperationResult<Annotation> result) {
				assertFalse("Errors when loading annotation", result.hasErrors());
				assertNotNull("Annotation reurned was null", result.getResult());
				assertEquals("Incorrect nid on the Annotation returned.", 1234, result.getResult().getId());
			}
			
		});
	}
}
