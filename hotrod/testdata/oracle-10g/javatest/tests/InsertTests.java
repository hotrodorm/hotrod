package tests;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;

import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.TestSequence1VO;
import hotrod.test.generation.TransactionVO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.TestSequence1DAO;
import hotrod.test.generation.primitives.TransactionDAO;

public class InsertTests {

  private static transient final Logger log = Logger.getLogger(InsertTests.class);

  public static void main(final String[] args) throws IOException, SQLException, DynamicSQLEvaluationException {

    log.info("Starting insert tests");

    insertUsingSequence();
    // insertByExampleUsingSequence();

    // insertNoAutogeneration();
    // insertByExampleNoAutogeneration();
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

  private static void insertByExampleUsingSequence() throws SQLException {

    System.out.println("insertUsingSequence:");
    System.out.println("====================");

    TransactionVO t = new TransactionVO();
    t.setAccountId(1234001);
    t.setSeqId(0);
    t.setTime("123");
    // t.setAmount(200); // unset!
    t.setFedBranchId(102L);

    TransactionDAO.insertByExample(t);

    for (TransactionVO tx : TransactionDAO.selectByExample(new TransactionVO())) {
      System.out.println("tx=" + tx);
    }
  }

  private static void insertByExampleNoAutogeneration() throws SQLException {

    System.out.println("insertByExampleNoAutogeneration:");
    System.out.println("================================");

    int node = (int) (System.currentTimeMillis() % (long) 100000000);

    ConfigValuesVO t = new ConfigValuesVO();

    t.setNode(node);
    t.setCell(2);
    t.setName("Node " + node);
    // t.setVerbatim("This is cell 1000"); // unset!

    ConfigValuesDAO.insertByExample(t);

    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(new ConfigValuesVO())) {
      System.out.println("v=" + v);
    }
  }

}
