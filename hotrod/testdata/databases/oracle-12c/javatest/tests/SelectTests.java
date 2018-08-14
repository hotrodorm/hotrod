package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import hotrod.test.generation.Account2VO;
import hotrod.test.generation.AccountVO;
import hotrod.test.generation.Alert2VO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.TxBranchVO;
import hotrod.test.generation._price_VO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountOrderBy;
import hotrod.test.generation.primitives.AlertFinder;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.HouseDAO;
import hotrod.test.generation.primitives.TxBranchDAO;
import hotrod.test.generation.primitives._price_DAO;

public class SelectTests {

  public static void main(final String[] args) throws IOException, SQLException {
    countProperties();
  }

  private static void countProperties() throws SQLException {
    // selectByExample();
    // selectByUI();
    // selectSortedByName();
    // selectByExampleWithNull();
    // selectTag();
    // tryInsertBadData();
    // selectViewSequenceAndQuery();

    // selectOtherSchema();
    // selectMultiSchema();
    selectComplexName();

  }

  private static void tryInsertBadData() throws SQLException {
    AccountVO a = new AccountVO();
    AccountDAO.insert(a);
  }

  private static void selectByExample() throws SQLException {
    TxBranchVO example = new TxBranchVO();
    example.setBranchId(103);
    for (TxBranchVO tb : TxBranchDAO.selectByExample(example)) {
      System.out.println("tb=" + tb);
    }
  }

  private static void selectByUI() throws SQLException {
    ConfigValuesVO example = new ConfigValuesVO();
    example.setName("prop3");
    for (ConfigValuesVO v : ConfigValuesDAO.selectByExample(example)) {
      System.out.println("v: " + v);
    }

  }

  private static void selectByExampleWithNull() throws SQLException {
    AccountVO example = new AccountVO();
    example.setType("CHK");
    example.setCurrentBalance(null);

    for (AccountVO a : AccountDAO.selectByExample(example)) {
      System.out.println(a);
    }

  }

  private static void selectSortedByName() throws SQLException {
    AccountVO example = new AccountVO();

    System.out.println();
    System.out.println("--- All accounts (sorted by name) ---");
    for (AccountVO a : AccountDAO.selectByExample(example, AccountOrderBy.NAME)) {
      System.out.println("1: " + a);
    }

    System.out.println();
    System.out.println("--- All accounts (sorted by name, case insensitive) ---");
    for (AccountVO a : AccountDAO.selectByExample(example, AccountOrderBy.NAME$CASEINSENSITIVE)) {
      System.out.println("2: " + a);
    }

    System.out.println();
    System.out.println("--- All accounts (sorted by name, case insensitive, stable forward) ---");
    for (AccountVO a : AccountDAO.selectByExample(example, AccountOrderBy.NAME$CASEINSENSITIVE_STABLE_FORWARD)) {
      System.out.println("3: " + a);
    }

    System.out.println();
    System.out.println("--- All accounts (sorted by name, case insensitive, stable reverse) ---");
    for (AccountVO a : AccountDAO.selectByExample(example, AccountOrderBy.NAME$CASEINSENSITIVE_STABLE_REVERSE)) {
      System.out.println("4: " + a);
    }

  }

  private static void selectComplexName() throws SQLException {
    System.out.println("=== Complex Name ===");
    for (_price_VO p : _price_DAO.selectByExample(new _price_VO())) {
      System.out.println("p: " + p);
    }
    System.out.println("===");
  }

  private static void selectOtherSchema() throws SQLException {
    System.out.println("=== House ===");
    for (HouseVO h : HouseDAO.selectByExample(new HouseVO())) {
      System.out.println("h: " + h);
    }
    System.out.println("===");
  }

  private static void selectMultiSchema() throws SQLException {
    System.out.println("=== Tree ===");
    for (Account2VO acc : AlertFinder.findAlerts()) {
      System.out.println("account: " + acc);
      for (Alert2VO a : acc.getAlerts()) {
        System.out.println(" + alert: " + a);
        System.out.println(" + house: " + a.getHouse());
      }
    }
    System.out.println("===");
  }

  private static void selectViewSequenceAndQuery() throws SQLException {
    System.out.println("TxBranch:");
    System.out.println("=========");

    long codeSeq = TxBranchDAO.getCodeSequence();
    System.out.println("codeSeq=" + codeSeq);

    Date from = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").parse("20160101-000000",
        new java.text.ParsePosition(0));
    Date to = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").parse("20170101-000000",
        new java.text.ParsePosition(0));

    int rows = TxBranchDAO.applyAccountPromotion75(10, from, to, -1);
    System.out.println("promotios rows=" + rows);

  }

}
