package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;

import hotrod.test.generation.HeftyAccountVO;
import hotrod.test.generation.primitives.HeftyAccountDAO;

public class UpdateViewsTests {

  private static transient final Logger log = Logger.getLogger(UpdateViewsTests.class);

  public static void main(final String[] args) throws IOException, SQLException, DynamicSQLEvaluationException {

    log.info("Starting view update");

    // insertHeftyAccount();
    // updateHeftyAccount();
    deleteHeftyAccount();
  }

  private static void insertHeftyAccount() throws SQLException {

    System.out.println("insert in View:");
    System.out.println("===============");

    HeftyAccountVO example = new HeftyAccountVO();
    example.setId(125);
    example.setName("ACC801");
    example.setType("CHK");
    example.setCurrentBalance(75000);
    example.setCreatedOn(new Date());
    example.setRowVersion(123);

    int rows = HeftyAccountDAO.insertByExample(example);
    System.out.println("---> INSERTED: rows=" + rows);

    for (HeftyAccountVO ha : HeftyAccountDAO.selectByExample(new HeftyAccountVO())) {
      System.out.println("ha=" + ha);
    }
  }

  private static void updateHeftyAccount() throws SQLException {

    System.out.println("update in View:");
    System.out.println("===============");

    HeftyAccountVO example = new HeftyAccountVO();
    example.setId(125);

    HeftyAccountVO updateValues = new HeftyAccountVO();
    updateValues.setType("SAV");
    updateValues.setCreatedOn(null);

    int rows = HeftyAccountDAO.updateByExample(example, updateValues);
    System.out.println("---> UPDATED: rows=" + rows);

    for (HeftyAccountVO ha : HeftyAccountDAO.selectByExample(new HeftyAccountVO())) {
      System.out.println("ha=" + ha);
    }
  }

  private static void deleteHeftyAccount() throws SQLException {

    System.out.println("delete in View:");
    System.out.println("===============");

    HeftyAccountVO example = new HeftyAccountVO();
    example.setId(125);

    int rows = HeftyAccountDAO.deleteByExample(example);
    System.out.println("---> DELETED: rows=" + rows);

    for (HeftyAccountVO ha : HeftyAccountDAO.selectByExample(new HeftyAccountVO())) {
      System.out.println("ha=" + ha);
    }
  }

}
