package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.ConfigValuesDAO;
import hotrod.test.generation.TxBranchDAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {
    selectByExample();
    // selectByUI();
  }

  private static void selectByExample() throws SQLException {
    TxBranchDAO example = new TxBranchDAO();
    example.setBranchId(103);
    for (TxBranchDAO tb : TxBranchDAO.selectByExample(example)) {
      System.out.println("tb: " + tb);
    }
  }

  private static void selectByUI() throws SQLException {
    ConfigValuesDAO example = new ConfigValuesDAO();
    example.setName("prop3");
    for (ConfigValuesDAO v : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("v: " + v);
    }

    // System.out.println("===");
    // for (MultParamSelectDAO mp : MultParamSelectDAO.select(100)) {
    // System.out.println("mp=" + mp);
    // }
  }

}
