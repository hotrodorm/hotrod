package automatedtests.identifiers;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class AllIdentifierTests {

  public static Test suite() {
    TestSuite ts = new TestSuite();

    ts.addTestSuite(IdFromJavaTests.class);
    ts.addTestSuite(IdFromSQLTests.class);
    ts.addTestSuite(IdFromSQLWithJavaTests.class);
    ts.addTestSuite(IdSplitTests.class);
    ts.addTestSuite(ObjectIdTests.class);
    ts.addTestSuite(SQLNameTests.class);

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
