package automatedtests;

import automatedtests.identifiers.IdentifierTests;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class AllAutomatedTests {

  public static Test suite() {
    TestSuite ts = new TestSuite();

    ts.addTestSuite(IdentifierTests.class);

    return ts;
  }

  public static void main(final String[] args) {
    System.out.println("Starting tests...");
    Test ts = suite();
    TestResult tr = junit.textui.TestRunner.run(ts);
    if (!tr.wasSuccessful()) {
      System.exit(1);
    }
  }

}
