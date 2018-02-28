package tests;

import java.io.IOException;
import java.sql.SQLException;

import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.TxBranchVO;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.TxBranchDAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {
    selectByExample();
    // selectByUI();
  }

  private static void selectByExample() throws SQLException {
    TxBranchVO example = new TxBranchVO();
    example.setBranchId(103);
    for (TxBranchVO tb : TxBranchDAO.selectByExample(example)) {
      System.out.println("tb: " + tb);
    }
  }

  private static void selectByUI() throws SQLException {
    ConfigValuesVO example = new ConfigValuesVO();
    example.setName("prop3");
    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("v: " + v);
    }

    // System.out.println("===");
    // for (MultParamSelectVO mp : MultParamSelectDAO.select(100)) {
    // System.out.println("mp=" + mp);
    // }
  }

}
