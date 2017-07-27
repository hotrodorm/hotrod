package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;

import hotrod.test.generation.AccountByIdsVO;
import hotrod.test.generation.AccountTx3VO;
import hotrod.test.generation.AccountVO;
import hotrod.test.generation.HouseVO;
import hotrod.test.generation.accounting.finances.QuadrantVO;
import hotrod.test.generation.accounting.finances.SearchedAccount2VO;
import hotrod.test.generation.accounting.finances.SearchedAccountVO;
import hotrod.test.generation.accounting.finances.primitives.QuadrantDAO;
import hotrod.test.generation.accounting.finances.primitives.SearchedAccount;
import hotrod.test.generation.accounting.finances.primitives.SearchedAccount2;
import hotrod.test.generation.primitives.AccountByIds;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountTx3;
import hotrod.test.generation.primitives.House;
import hotrod.test.generation.primitives.TxBranchDAO;
import hotrod.test.generation.primitives.TypesOtherDAO;

public class SelectTests {

  private static transient final Logger log = Logger.getLogger(SelectTests.class);

  public static void main(final String[] args) throws IOException, SQLException, DynamicSQLEvaluationException {

    log.info("Starting tests");

    // selectByExample();
    // selectByUI();
    // selectSortedByName();
    // selectByExampleWithNull();
    // selectTag();
    // tryInsertBadData();
    // selectViewSequenceAndQuery();

    // apply74();

    // searchAccounts();

    // searchAccountsByIds();
    // evaluateAccountsByIds();

    // searchAccount();
    // searchAccount2();
    // selectAccountTx3();
    // specialCharactersInNames();

    searchQuadrants();

  }

  // private static void tryInsertBadData() throws SQLException {
  // AccountDAO a = new AccountDAO();
  // a.insert();
  // }
  //
  // private static void selectByExample() throws SQLException {
  // TxBranchDAO example = new TxBranchDAO();
  // example.setBranchId(103);
  // for (TxBranchDAO tb : TxBranchDAO.selectByExample(example)) {
  // System.out.println("tb=" + tb);
  // }
  // }
  //
  // private static void selectByUI() throws SQLException {
  // ConfigValuesDAO example = new ConfigValuesDAO();
  // example.setName("prop3");
  // for (ConfigValuesDAO v : ConfigValuesDAO.selectByExample(example)) {
  // System.out.println("v: " + v);
  // }
  //
  // // System.out.println("===");
  // // for (MultParamSelectDAO mp : MultParamSelectDAO.select(100)) {
  // // System.out.println("mp=" + mp);
  // // }
  // }
  //
  // private static void selectByExampleWithNull() throws SQLException {
  // AccountDAO example = new AccountDAO();
  // example.setType("CHK");
  // example.setCurrentBalance(null);
  //
  // for (AccountDAO a : AccountDAO.selectByExample(example)) {
  // System.out.println(a);
  // }
  //
  // }

  // private static void selectSortedByName() throws SQLException {
  // AccountDAO example = new AccountDAO();
  //
  // System.out.println();
  // System.out.println("--- All accounts (sorted by name) ---");
  // for (AccountDAO a : AccountDAO.selectByExample(example,
  // AccountDAOOrderBy.NAME)) {
  // System.out.println("1: " + a);
  // }
  //
  // System.out.println();
  // System.out.println("--- All accounts (sorted by name, case insensitive)
  // ---");
  // for (AccountDAO a : AccountDAO.selectByExample(example,
  // AccountDAOOrderBy.NAME$CASEINSENSITIVE)) {
  // System.out.println("2: " + a);
  // }
  //
  // System.out.println();
  // System.out.println("--- All accounts (sorted by name, case insensitive,
  // stable forward) ---");
  // for (AccountDAO a : AccountDAO.selectByExample(example,
  // AccountDAOOrderBy.NAME$CASEINSENSITIVE_STABLE_FORWARD)) {
  // System.out.println("3: " + a);
  // }
  //
  // System.out.println();
  // System.out.println("--- All accounts (sorted by name, case insensitive,
  // stable reverse) ---");
  // for (AccountDAO a : AccountDAO.selectByExample(example,
  // AccountDAOOrderBy.NAME$CASEINSENSITIVE_STABLE_REVERSE)) {
  // System.out.println("4: " + a);
  // }
  //
  // }

  // private static void selectTag() throws SQLException {
  // System.out.println("AccountTx3:");
  // System.out.println("===========");
  // for (AccountTx3 a : AccountTx3.select(50, 300)) {
  // System.out.println("--> AccountTx3 = " + a);
  // }
  // }

  private static void selectViewSequenceAndQuery() throws SQLException {
    System.out.println("TxBranch:");
    System.out.println("=========");

    long codeSeq = TxBranchDAO.getCodeSequence();
    System.out.println("codeSeq=" + codeSeq);

  }

  private static void apply74() throws SQLException {
    System.out.println("Apply74:");
    System.out.println("========");

    Integer reward = 5;

    Date from = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").parse("20160101-000000",
        new java.text.ParsePosition(0));
    Date to = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").parse("20170101-000000",
        new java.text.ParsePosition(0));

    Integer minBalance = 100;
    Integer maxBalance = 300;
    // String type = "CHK";
    String type = null;

    int rows = TypesOtherDAO.applyAccountPromotion74(reward, from, to, minBalance, maxBalance, type);
    System.out.println("promotions rows=" + rows);

  }

  private static void searchAccounts() throws SQLException {
    System.out.println("searchAccounts:");
    System.out.println("===============");
    AccountVO example = new AccountVO();
    example.setId(10);
    for (AccountVO a : AccountDAO.selectByExample(example)) {
      System.out.println("a=" + a);
    }
  }

  private static void searchAccountsByIds() throws SQLException {
    System.out.println("searchAccountsByIds:");
    System.out.println("====================");

    // String type = "CHK";
    String type = null;
    String namePattern = "%ACC%";
    // String namePattern = null;
    Integer minBalance = 123;
    Integer maxBalance = null;

    List<Integer> ids = new ArrayList<Integer>();
    // ids.add(5);
    // ids.add(6);
    // ids.add(7);
    // ids.add(8);
    // ids.add(9);
    // ids.add(10);

    Integer sortType = null;

    for (AccountByIdsVO a : AccountByIds.select(ids, minBalance, maxBalance, namePattern, type, sortType)) {
      System.out.println("a=" + a);
    }
  }

  // private static void evaluateAccountsByIds() throws SQLException,
  // DynamicSQLEvaluationException {
  // System.out.println("searchAccountsByIds:");
  // System.out.println("====================");
  //
  // String name = "abc";
  // String type = null;
  // Integer minBalance = null;
  // Integer maxBalance = null;
  //
  // List<Integer> ids = new ArrayList<Integer>();
  // ids.add(5);
  // ids.add(6);
  // ids.add(7);
  // ids.add(8);
  // ids.add(9);
  // ids.add(10);
  //
  // Integer sortType = 2;
  //
  // DynamicSQLParameters params = new DynamicSQLParameters();
  // params.set("name", name);
  // params.set("type", type);
  // params.set("minBalance", minBalance);
  // params.set("maxBalance", maxBalance);
  // params.set("ids", ids);
  // params.set("sortType", sortType);
  //
  // String sql = AccountByIds.JAVA_EXPRESSION.evaluate(params);
  // System.out.println("sql=" + sql);
  //
  // }

  private static void selectAccountTx3() throws SQLException {
    System.out.println("AccountTx3:");
    System.out.println("===========");
    for (AccountTx3VO a : AccountTx3.select(200, 500)) {
      System.out.println("a=" + a);
    }
  }

  private static void searchAccount() throws SQLException {
    System.out.println("SearchedAccount:");
    System.out.println("================");
    for (SearchedAccountVO a : SearchedAccount.select(50, 150, 2)) {
      System.out.println("a=" + a);
    }
  }

  private static void searchAccount2() throws SQLException {
    System.out.println("SearchedAccount2:");
    System.out.println("=================");
    for (SearchedAccount2VO a : SearchedAccount2.select(150)) {
      System.out.println("a=" + a);
    }
  }

  private static void searchQuadrants() throws SQLException {
    System.out.println("SearchedQuadrants:");
    System.out.println("==================");
    for (QuadrantVO q : QuadrantDAO.selectByExample(new QuadrantVO())) {
      System.out.println("q=" + q);
    }
  }

  private static void specialCharactersInNames() throws SQLException {
    System.out.println("specialCharactersInNames:");
    System.out.println("=========================");

    // Special2VO s2 = Special2DAO.select(1);
    // System.out.println("s2=" + s2);

    // HouseVO h1 = House.select(1);
    // System.out.println("h1=" + h1);

    for (HouseVO h : House.selectByExample(new HouseVO())) {
      System.out.println("h=" + h);
    }
  }

}
