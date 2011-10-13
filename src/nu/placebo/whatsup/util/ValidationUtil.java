package nu.placebo.whatsup.util;

public class ValidationUtil {
	public static boolean emailIsValid(String email) {

		return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
	}
}
