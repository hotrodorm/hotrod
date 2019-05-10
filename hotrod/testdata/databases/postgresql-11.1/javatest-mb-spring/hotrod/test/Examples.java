package hotrod.test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import hotrod.test.generation.Account;
import hotrod.test.generation.TxBranch;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountOrderBy;
import hotrod.test.generation.primitives.ClientDAO;
import hotrod.test.generation.primitives.TxBranchDAO;
import hotrod.test.generation.primitives.TxBranchDAO.TxBranchOrderBy;

public class Examples {

  public static void main(final String[] args) throws IOException, SQLException {
    Examples ex = new Examples();
    // ex.runExamples();
    // ex.runLiveSQL();
    ex.runSelectbyCriteria();
  }

  private void runLiveSQL() throws SQLException {

    ClientDAO dao = SpringBeanRetriever.getBean("clientDAO");

    AccountDAO.Account a = AccountDAO.newTable("a");

    // dao.createSelect()

    List<Map<String, Object>> rows = dao //
        .createSelect(a.createdOn, a.name, a.currentBalance, a.mainStatus, a.id, a.type) //
        .from(a) //
        .where(a.mainStatus.eq(1)) //
        .and(a.currentBalance.lt(100)) //
        .or(a.type.ne("S'AV")) //
        .orderBy(a.createdOn.asc()) //
        .execute() //
    ;

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

  }

  private void runSelectbyCriteria() throws SQLException {

    AccountDAO dao = SpringBeanRetriever.getBean("accountDAO");
    AccountDAO.Account a = AccountDAO.newTable();
    List<Account> rows = dao.selectByCriteria(a.currentBalance.gt(100)) //
        .execute();

    for (Account r : rows) {
      System.out.println("row: " + r);
    }

  }

  private void runExamples() throws SQLException {

    AccountDAO dao = SpringBeanRetriever.getBean("accountDAO");

    // Select by PK

    {
      Account a = dao.selectByPK(1004);
      System.out.println("Test #1: a=" + a);
    }

    // Select by UI

    {
      Account a = dao.selectByUIName("CHK1004");
      System.out.println("Test #2: a=" + a);
      System.out.println("Test #2 JSON: a=" + (a == null ? "null" : a.toJSON()));
    }

    // Select by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      Account example = new Account();
      example.setCreatedOn(creationDate);
      List<Account> accounts = dao.selectByExample(example);
      if (accounts.isEmpty()) {
        System.out.println("Test #3: --- No accounts found.");
      } else {
        for (Account a : accounts) {
          System.out.println("Test #3: a=" + a);
        }
      }
    }

    // Select by example with order by

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      Account example = new Account();
      example.setCreatedOn(creationDate);
      List<Account> accounts = dao.selectByExample(example, AccountOrderBy.CURRENT_BALANCE$DESC, AccountOrderBy.NAME);
      if (accounts.isEmpty()) {
        System.out.println("Test #4: --- No accounts found.");
      } else {
        for (Account a : accounts) {
          System.out.println("Test #4: a=" + a);
        }
      }
    }

    // insert

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      Account a = new Account();
      // a.setId(xxx); // ignored
      a.setName("CHK4010");
      a.setType("CHK");
      a.setCurrentBalance(0);
      a.setCreatedOn(creationDate);
      // a.setActive(1);
      dao.insert(a);
      System.out.println("Test #5 - new account id is: " + a.getId());
    }

    // update the balance

    {
      Account a = dao.selectByPK(1234004);
      a.setCurrentBalance(250);
      int rows = dao.update(a);
      System.out.println("Test #6 - updated rows=" + rows);
    }

    // update by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      Account example = new Account();
      example.setCreatedOn(creationDate);
      Account updateValues = new Account();
      updateValues.setCurrentBalance(0);
      int rows = dao.updateByExample(example, updateValues);
      System.out.println("Test #7 - updated rows=" + rows);
    }

    // delete

    {
      Account a = new Account();
      a.setId(104);
      int rows = dao.delete(a);
      System.out.println("Test #8 - deleted rows=" + rows);
    }

    // delete by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      Account example = new Account();
      example.setCreatedOn(creationDate);
      int rows = dao.deleteByExample(example);
      System.out.println("Test #9 - updated rows=" + rows);
    }

    // Views

    {
      TxBranchDAO tdao = SpringBeanRetriever.getBean("txBranchDAO");
      TxBranch example = new TxBranch();
      example.setBranchId(681);
      List<TxBranch> txs = tdao.selectByExample(example, TxBranchOrderBy.ACCOUNT_ID, TxBranchOrderBy.AMOUNT);
      for (TxBranch b : txs) {
        System.out.println("Test #10: b=" + b);
      }
    }

    // Selects

  }

}
