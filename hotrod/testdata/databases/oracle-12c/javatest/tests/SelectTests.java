package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import hotrod.test.generation.AccountTx3VO;
import hotrod.test.generation.AccountVO;
import hotrod.test.generation.ConfigValuesVO;
import hotrod.test.generation.TxBranchVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountOrderBy;
import hotrod.test.generation.primitives.AccountTx3;
import hotrod.test.generation.primitives.ConfigValuesDAO;
import hotrod.test.generation.primitives.TxBranchDAO;

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
    selectViewSequenceAndQuery();
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

  private static void selectTag() throws SQLException {
    System.out.println("AccountTx3:");
    System.out.println("===========");
    for (AccountTx3VO a : AccountTx3.select(50, 300)) {
      System.out.println("--> AccountTx3 = " + a);
    }
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
