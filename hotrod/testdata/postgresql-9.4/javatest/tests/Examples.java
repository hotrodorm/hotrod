package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.TxBranchVO;
import hotrod.test.generation.UnusualTxVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountOrderBy;
import hotrod.test.generation.primitives.TxBranchDAO;
import hotrod.test.generation.primitives.TxBranchDAO.TxBranchOrderBy;
import hotrod.test.generation.primitives.UnusualTxDAO;

public class Examples {

  public static void main(final String[] args) throws IOException, SQLException {
    examples();
  }

  private static void examples() throws SQLException {

    // Select by PK

    {
      AccountVO a = AccountDAO.select(1004);
    }

    // Select by UI

    {
      AccountVO a = AccountDAO.selectByUIName("CHK1004");
    }

    // Select by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      List<AccountVO> accounts = AccountDAO.selectByExample(example);
    }

    // Select by example with order by

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      List<AccountVO> accounts = AccountDAO.selectByExample(example, AccountOrderBy.CURRENT_BALANCE$DESC,
          AccountOrderBy.NAME);
    }

    // insert

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO a = new AccountVO();
      // a.setId(xxx); // ignored
      a.setName("CHK4010");
      a.setCreatedOn(creationDate);
      a.setCurrentBalance(0);
      AccountDAO.insert(a);
      System.out.println("New account id is: " + a.getId());
    }

    // update the balance

    {
      AccountVO a = AccountDAO.select(1004);
      a.setCurrentBalance(250);
      int rows = AccountDAO.update(a);
    }

    // update by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      AccountVO updateValues = new AccountVO();
      updateValues.setCurrentBalance(0);
      int rows = AccountDAO.updateByExample(example, updateValues);
    }

    // delete

    {
      AccountVO a = new AccountVO();
      a.setId(104);
      int rows = AccountDAO.delete(a);
    }

    // delete by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      int rows = AccountDAO.deleteByExample(example);
    }

    // Views

    {
      TxBranchVO example = new TxBranchVO();
      example.setBranchId(681);
      List<TxBranchVO> txs = TxBranchDAO.selectByExample(example, TxBranchOrderBy.ACCOUNT_ID, TxBranchOrderBy.AMOUNT);
    }

    // Selects

    {
      List<UnusualTxVO> utxs = UnusualTxDAO.select(300, 50, 1000, 4000);
    }

  }

}
