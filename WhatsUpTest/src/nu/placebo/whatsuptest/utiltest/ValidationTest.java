package nu.placebo.whatsuptest.utiltest;

import nu.placebo.whatsup.util.ValidationUtil;
import android.test.AndroidTestCase;

public class ValidationTest extends AndroidTestCase {

	public void setUp() throws Exception {
		super.setUp();
	}

	public void testValidEmail() {
		String email = "kirAya_tail@emaIl.com";

		assertTrue("Valid email", ValidationUtil.emailIsValid(email));
	}

	public void testInvalidEmail() {
		String[] emails = { "ki#€ata@email.com", "asdf.d.mail.com",
				"asdf@asdf", "asdf@.com", "asdf@asdf.abcdefghi", "asdf@asdf.2de" };
		
		for(int i=0; i< emails.length; i++){
			assertFalse("Got through: "+emails[i], ValidationUtil.emailIsValid(emails[i]));
		}
	}

}
