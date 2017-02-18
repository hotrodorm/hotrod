package tests;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import hotrod.test.generation.AccountDAO;
import hotrod.test.generation.TransactionDAO;
import hotrod.test.generation.TxBranchDAO;
import hotrod.test.generation.UnusualTxDAO;
import hotrod.test.generation.primitives.AccountDAOPrimitives.AccountDAOOrderBy;
import hotrod.test.generation.primitives.TransactionDAOPrimitives.TransactionDAOOrderBy;
import hotrod.test.generation.primitives.TxBranchDAOPrimitives.TxBranchDAOOrderBy;

public class Examples {

  public static void main(final String[] args) throws IOException, SQLException {
    examples();
  }

  private static void examples() throws SQLException {

    // Select by PK

    {
      AccountDAO a = AccountDAO.select(1004);
    }

    // Select by UI

    {
      AccountDAO a = AccountDAO.selectByUIName("CHK1004");

      List<TransactionDAO> txs = a.selectChildrenTransactionDAO().byAccountId(TransactionDAOOrderBy.FED_BRANCH_ID,
          TransactionDAOOrderBy.AMOUNT$DESC);
    }

    // Select by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountDAO example = new AccountDAO();
      example.setCreatedOn(creationDate);
      List<AccountDAO> accounts = AccountDAO.selectByExample(example);
    }

    // Select by example with order by

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountDAO example = new AccountDAO();
      example.setCreatedOn(creationDate);
      List<AccountDAO> accounts = AccountDAO.selectByExample(example, AccountDAOOrderBy.CURRENT_BALANCE$DESC,
          AccountDAOOrderBy.NAME);
    }

    // insert

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountDAO a = new AccountDAO();
      // a.setId(xxx); // ommitted
      a.setName("CHK4010");
      a.setCreatedOn(creationDate);
      a.setCurrentBalance(0);
      a.insert();
      System.out.println("New account id is: " + a.getId());
    }

    // update the balance

    {
      AccountDAO a = AccountDAO.select(1004);
      a.setCurrentBalance(250);
      int rows = a.update();
    }

    // update by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountDAO example = new AccountDAO();
      example.setCreatedOn(creationDate);
      AccountDAO updateValues = new AccountDAO();
      updateValues.setCurrentBalance(0);
      int rows = AccountDAO.updateByExample(example, updateValues);
    }

    // delete

    {
      AccountDAO a = new AccountDAO();
      a.setId(104);
      int rows = a.delete();
    }

    // delete by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountDAO example = new AccountDAO();
      example.setCreatedOn(creationDate);
      int rows = AccountDAO.deleteByExample(example);
    }

    // Views

    {
      TxBranchDAO example = new TxBranchDAO();
      example.setBranchId(681);
      List<TxBranchDAO> txs = TxBranchDAO.selectByExample(example, TxBranchDAOOrderBy.ACCOUNT_ID,
          TxBranchDAOOrderBy.AMOUNT);
    }

    // Selects

    {
      List<UnusualTxDAO> utxs = UnusualTxDAO.select(300, 50, 1000, 4000);
    }

  }

}
