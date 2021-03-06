package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;

import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.TestMixedVO;
import hotrod.test.generation.TestSequence1VO;
import hotrod.test.generation.TestSequence2VO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.TestMixedDAO;
import hotrod.test.generation.primitives.TestSequence1DAO;
import hotrod.test.generation.primitives.TestSequence2DAO;

public class InsertTests {

  private static transient final Logger log = Logger.getLogger(InsertTests.class);

  public static void main(final String[] args) throws IOException, SQLException, DynamicSQLEvaluationException {

    log.info("Starting insert tests");

    // insertUsingSequence();
    // insertByExampleUsingSequence();

    // insertNoAutogeneration();
    // insertByExampleNoAutogeneration();
    insertMixed();
  }

  private static void insertUsingSequence() throws SQLException {

    System.out.println("insertUsingSequence:");
    System.out.println("====================");

    TestSequence1VO r = new TestSequence1VO();
    r.setName("Name 1");
    TestSequence1DAO.insert(r);

    for (TestSequence1VO row : TestSequence1DAO.selectByExample(new TestSequence1VO())) {
      System.out.println("row=" + row);
    }
  }

  private static void insertNoAutogeneration() throws SQLException {

    System.out.println("insertNoAutogeneration:");
    System.out.println("=======================");

    int node = (int) (System.currentTimeMillis() % (long) 100000000);

    ConfigValuesVO t = new ConfigValuesVO();

    t.setNode(node);
    t.setCell(2);
    t.setName("Node " + node);
    t.setVerbatim("This is cell 1000");

    ConfigValuesDAO.insert(t);

    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(new ConfigValuesVO())) {
      System.out.println("v=" + v);
    }
  }

  private static void insertMixed() throws SQLException {

    String time = getTime();
    int timeInt = getTimeInt();

    // Test sequences

    TestSequence2VO ti1 = new TestSequence2VO();
    ti1.setName("Title (default) " + time);
    TestSequence2DAO.insert(ti1);
    System.out.println("[inserted] sequence test=" + ti1);

    TestMixedVO a = new TestMixedVO();
    a.setName("Caption 007 - " + time);
    a.setPrice(50004);
    // a.setBranchId(123456);

    TestMixedDAO.insert(a);
    // TestMixedDAO.insert(a, true);
    System.out.println("[inserted] mixed=" + a);

  }

  // Utilities

  private static int getTimeInt() {
    return (int) (System.currentTimeMillis() % (1000000000L));
  }

  private static String getTime() {
    return new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
  }

}
