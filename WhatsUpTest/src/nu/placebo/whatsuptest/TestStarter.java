package nu.placebo.whatsuptest;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class TestStarter extends TestSuite {
	public static Test suite() {
		return new TestSuiteBuilder(TestStarter.class).includeAllPackagesUnderHere().build();
	}
}
