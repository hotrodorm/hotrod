package selects.structuredvos;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.runtime.util.SUtils;

import hotrod.test.generation.AccountPersonFlatVO;
import hotrod.test.generation.AccountPersonVO;
import hotrod.test.generation.AccountPersonVO4;
import hotrod.test.generation.AccountVO;
import hotrod.test.generation.AccountWithTransactions2VO;
import hotrod.test.generation.AccountWithTransactionsVO;
import hotrod.test.generation.ExpandedAccountVO;
import hotrod.test.generation.ExtendedPersonVO;
import hotrod.test.generation.FullAccountVO;
import hotrod.test.generation.FullPersonVO;
import hotrod.test.generation.LogVO;
import hotrod.test.generation.LogWithOfficeVO;
import hotrod.test.generation.NorthOfficeVO;
import hotrod.test.generation.OfficeWithLogVO;
import hotrod.test.generation.TransactionVO;
import hotrod.test.generation.VIPAccountVO;
import hotrod.test.generation.ValuatedAccountVO;
import hotrod.test.generation.primitives.PersonDAO;
import hotrod.test.generation.primitives.Test2;

public class StructuredTests {

  public void run() throws SQLException {
    // test1();
    // test2();
    // test3();
    // test4();
    // test5();
    // test6();
    test7();
    // test8();
    // test9();
    // test10();
    // test11();
    // test12(); // bad test
  }

  // Case 1 - Flat Join

  private static void test1() throws SQLException {
    System.out.println("=== Extended Persons ===");
    for (AccountPersonFlatVO ap : PersonDAO.findAccountWithPersonFlat(null)) {
      System.out.println("ap=" + ap);
    }
  }

  // Case 2 - Select returns multiple VOs

  private static void test2() throws SQLException {
    System.out.println("=== Account With Person ===");
    for (AccountPersonVO ap : PersonDAO.findAccountWithPerson(null)) {
      printObject(ap, 2);
      printObject(ap.getAccount(), 4);
      printObject(ap.getPerson(), 4);
    }
  }

  // Case 3 - Select returns a single VO and no expressions

  private static void test3() throws SQLException {
    System.out.println("=== Retirement Account ===");
    for (AccountVO a : PersonDAO.findRetirementAccount(null)) {
      printObject(a, 2);
    }
  }

  // Case 4 - Extra columns

  private static void test4() throws SQLException {
    System.out.println("=== Valuated Account ===");
    for (ValuatedAccountVO va : PersonDAO.findValuatedAccount(null)) {
      printObject(va, 2);
      printObject(va.getAccount(), 4);
      printObject(va.getPerson(), 4);
    }
  }

  // Case 5 - Collections

  private static void test5() throws SQLException {
    System.out.println("=== Account With Transaction ===");
    for (AccountWithTransactionsVO at : PersonDAO.findAccountWithTransactions(null)) {
      printObject(at, 2);
      for (TransactionVO t : at.getTransactions()) {
        printObject(t, 4);
      }
    }
  }

  // Case 6 - Associations

  private static void test6() throws SQLException {
    System.out.println("=== VIP Account ===");
    for (VIPAccountVO va : PersonDAO.findVIPAccount(3, 500)) {
      printObject(va, 2);
      printObject(va.getPerson(), 4);
    }
  }

  // Case 7 - Collections and associations combined

  private static void test7() throws SQLException {
    List<ExtendedPersonVO> list = PersonDAO.findExtendedPerson(null);
    System.out.println("=== Extended Person ===");
    for (ExtendedPersonVO ep : list) {
      printObject(ep, 0);
      printObject(ep.getCategory(), 2);
      for (AccountWithTransactions2VO at : ep.getAccounts()) {
        printObject(at, 2);
        for (TransactionVO t : at.getTransactions()) {
          printObject(t, 4);
        }
      }
    }
  }

  // Case 8 - Associations & collections without PKs

  private static void test8() throws SQLException {
    System.out.println("=== Single Expanded Account ===");
    LogWithOfficeVO lo = PersonDAO.findSingleExpandedAccount();
    if (lo != null) {
      printObject(lo, 2);
      for (NorthOfficeVO no : lo.getOffice()) {
        printObject(no, 4);
      }
    }
  }

  // Case 9 - Select returns a single row

  private static void test9() throws SQLException {
    System.out.println("=== Single Expanded Account 2 ===");
    ExpandedAccountVO ea = PersonDAO.findSingleExpandedAccount2(53);
    if (ea != null) {
      printObject(ea.getAccount(), 2);
      printObject(ea.getPerson(), 2);
    } else {
      System.out.println("<no row found>");
    }
  }

  // Case 10 - Ids

  private static void test10() throws SQLException {
    System.out.println("=== Single Expanded Account 3 ===");
    OfficeWithLogVO lo = PersonDAO.findSingleExpandedAccount3("Nebraska");
    printObject(lo, 0);
    for (LogVO l : lo.getLogs()) {
      printObject(l, 2);
    }
  }

  // Case 11 - Multiples tests

  private static void test11() throws SQLException {
    System.out.println("=== Full Person ===");
    List<FullPersonVO> persons = PersonDAO.findFullPerson(null);
    System.out.println(" --> persons=" + persons.size());
    for (FullPersonVO ep : persons) {
      printObject(ep, 0);
      printObject(ep.getCategory(), 2);
      printObject(ep.getPerson(), 2);
      System.out.println(" --> accounts=" + ep.getPerson().getAccounts().size());
      for (FullAccountVO at : ep.getPerson().getAccounts()) {
        printObject(at, 2);
        for (TransactionVO t : at.getTransactions()) {
          printObject(t, 4);
        }
      }
    }
  }

  // Case 12 - Multiples tests

  private static void test12() throws SQLException {
    System.out.println("=== Full Person ===");
    List<AccountPersonVO4> persons = Test2.findAP(null);
    System.out.println(" --> persons=" + persons.size());
    for (AccountPersonVO4 ep : persons) {
      printObject(ep, 0);
      printObject(ep.getAccount(), 2);
      printObject(ep.getPerson(), 2);
    }
  }

  // Utilitites

  private static void printObject(final Object obj, final int indent) {
    if (obj == null) {
      System.out.println(SUtils.getFiller(' ', indent) + "null");
    } else {
      System.out.println(SUtils.indent(obj.toString(), indent));
    }
  }

}
