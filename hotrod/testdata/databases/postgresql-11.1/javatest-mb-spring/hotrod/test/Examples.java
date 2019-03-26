package hotrod.test;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.TxBranchVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountOrderBy;
import hotrod.test.generation.primitives.TxBranchDAO;
import hotrod.test.generation.primitives.TxBranchDAO.TxBranchOrderBy;

public class Examples {

  public static void main(final String[] args) throws IOException, SQLException {
    Examples ex = new Examples();
    ex.runExamples();
  }

  private void runExamples() throws SQLException {

    AccountDAO dao = SpringBeanRetriever.getBean("accountDAO");

    // Select by PK

    {
      AccountVO a = dao.selectByPK(1004);
      System.out.println("Test #1: a=" + a);
    }

    // Select by UI

    {
      AccountVO a = dao.selectByUIName("CHK1004");
      System.out.println("Test #2: a=" + a);
    }

    // Select by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      List<AccountVO> accounts = dao.selectByExample(example);
      for (AccountVO a : accounts) {
        System.out.println("Test #3: a=" + a);
      }
    }

    // Select by example with order by

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      List<AccountVO> accounts = dao.selectByExample(example, AccountOrderBy.CURRENT_BALANCE$DESC, AccountOrderBy.NAME);
      for (AccountVO a : accounts) {
        System.out.println("Test #4: a=" + a);
      }
    }

    // insert

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO a = new AccountVO();
      // a.setId(xxx); // ignored
      a.setName("CHK4010");
      a.setType("CHK");
      a.setCurrentBalance(0);
      a.setCreatedOn(creationDate);
      a.setActive(1);
      dao.insert(a);
      System.out.println("Test #5 - new account id is: " + a.getId());
    }

    // update the balance

    {
      AccountVO a = dao.selectByPK(1234004);
      a.setCurrentBalance(250);
      int rows = dao.update(a);
      System.out.println("Test #6 - updated rows=" + rows);
    }

    // update by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      AccountVO updateValues = new AccountVO();
      updateValues.setCurrentBalance(0);
      int rows = dao.updateByExample(example, updateValues);
      System.out.println("Test #7 - updated rows=" + rows);
    }

    // delete

    {
      AccountVO a = new AccountVO();
      a.setId(104);
      int rows = dao.delete(a);
      System.out.println("Test #8 - deleted rows=" + rows);
    }

    // delete by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      int rows = dao.deleteByExample(example);
      System.out.println("Test #9 - updated rows=" + rows);
    }

    // Views

    {
      TxBranchDAO tdao = SpringBeanRetriever.getBean("txBranchDAO");
      TxBranchVO example = new TxBranchVO();
      example.setBranchId(681);
      List<TxBranchVO> txs = tdao.selectByExample(example, TxBranchOrderBy.ACCOUNT_ID, TxBranchOrderBy.AMOUNT);
      for (TxBranchVO b : txs) {
        System.out.println("Test #10: b=" + b);
      }
    }

    // Selects

  }

}
