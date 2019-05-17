package hotrod.test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.SQL;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.hotrod.runtime.util.HexaUtils;
import org.springframework.beans.factory.annotation.Autowired;

import hotrod.test.generation.Account;
import hotrod.test.generation.TypesBinary;
import hotrod.test.generation.TypesOther;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountOrderBy;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;
import hotrod.test.generation.primitives.ClientDAO;
import hotrod.test.generation.primitives.ClientDAO.ClientTable;
import hotrod.test.generation.primitives.TransactionDAO;
import hotrod.test.generation.primitives.TransactionDAO.TransactionTable;
import hotrod.test.generation.primitives.TypesBinaryDAO;
import hotrod.test.generation.primitives.TypesBinaryDAO.TypesBinaryTable;
import hotrod.test.generation.primitives.TypesOtherDAO;
import hotrod.test.generation.primitives.TypesOtherDAO.TypesOtherTable;

public class UIServices {

  @Autowired
  private ClientDAO clientDao;

  @Autowired
  private AccountDAO accountDao;

  @Autowired
  private TypesBinaryDAO typesBinaryDao;

  @Autowired
  private TypesOtherDAO typesOtherDao;

  public void runLiveSQL() throws SQLException {

    AccountTable a = AccountDAO.newTable();
    ClientTable c = ClientDAO.newTable("c");

    List<Map<String, Object>> rows = this.clientDao //
        .createSelect(a.createdOn,

            a.name.isNull(),

            SQL.sum(a.currentBalance).over().partitionBy(a.type).orderBy(a.name.substr(5, 2).asc()).end()
                .as("runningBalance"),

            a.currentBalance.mult(a.id).lt(10),

            SQL.val(1.10).mult(a.currentBalance).as("cbp"), //
            a.currentBalance.trunc(-2).as("tb"), //
            a.createdOn.extract(DateTimeField.MONTH).as("month"), //
            a.name.coalesce(a.type).coalesce("N/A").as("xname"), //
            SQL.val("%").concat(a.name).concat(a.type).concat("%").as("like1"), //
            a.currentBalance, a.mainStatus, a.id, a.type) //
        .from(a) //
        .where(a.mainStatus.eq(1) //
            .and(a.currentBalance.lt(100)) //
            .or(a.type.ne("S'AV") //
                .and(SQL.exists(this.clientDao.createSelect().from(c).where(c.friendId.isNotNull())))) //
            .andNot(a.mainStatus.ne(14).or(a.createdOn.gt(SQL.currentDateTime())))) //
        .orderBy(a.createdOn.asc()) //
        .execute() //
    ;

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

  }

  public void runSelectbyCriteria() throws SQLException {
    AccountTable a = AccountDAO.newTable();
    List<Account> rows = this.accountDao.selectByCriteria(a, //
        a.currentBalance.gt(100) //
            .and(a.name.like("CHK%"))) //
        .execute();
    for (Account r : rows) {
      System.out.println("row: " + r);
    }
  }

  public void runSelectbyCriteriaBinary() throws SQLException {
    byte[] searched = new byte[2];
    searched[0] = 0x31;
    searched[1] = 0x35;
    TypesBinaryTable b = TypesBinaryDAO.newTable();
    List<TypesBinary> rows = this.typesBinaryDao.selectByCriteria(b, b.bin1.eq(searched)) //
        .execute();
    for (TypesBinary r : rows) {
      System.out.println("row: [" + r.getBol1() + ", " + HexaUtils.toHexa(r.getBin1()) + "]");
    }
  }

  public void runSelectbyCriteriaUUID() throws SQLException {
    byte[] searched = new byte[2];
    searched[0] = 0x31;
    searched[1] = 0x35;
    TypesOtherTable b = TypesOtherDAO.newTable();
    List<TypesOther> rows = this.typesOtherDao.selectByCriteria(b, b.uui1.ne("33bb9554-c616-42e6-a9c6-88d3bba4221c")) //
        .execute();

    for (TypesOther r : rows) {
      System.out.println("row: [" + r.toJSON() + "]");
    }
  }

  public void runSelectbyCriteriaIn() throws SQLException {
    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");
    List<Account> rows = this.accountDao.selectByCriteria(a, a.id.in( //
        this.accountDao.createSelect(t.accountId) //
            .from(t) //
            .where(t.accountId.eq(a.id).andNot(t.amount.ge(100).or(t.time.isNull())))) //
        .and(a.id.notIn(123, 456, 789))) //
        .execute();
    for (Account r : rows) {
      System.out.println("row: " + r);
    }
  }

  public void runExamples() throws SQLException {

    // Select by PK

    {
      Account a = this.accountDao.selectByPK(1004);
      System.out.println("Test #1: a=" + a);
    }

    // Select by UI

    {
      Account a = this.accountDao.selectByUIName("CHK1004");
      System.out.println("Test #2: a=" + a);
      System.out.println("Test #2 JSON: a=" + (a == null ? "null" : a.toJSON()));
    }

    // Select by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      Account example = new Account();
      example.setCreatedOn(creationDate);
      List<Account> accounts = this.accountDao.selectByExample(example);
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
      List<Account> accounts = this.accountDao.selectByExample(example, AccountOrderBy.CURRENT_BALANCE$DESC,
          AccountOrderBy.NAME);
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
      this.accountDao.insert(a);
      System.out.println("Test #5 - new account id is: " + a.getId());
    }

    // update the balance

    {
      Account a = this.accountDao.selectByPK(1234004);
      a.setCurrentBalance(250);
      int rows = this.accountDao.update(a);
      System.out.println("Test #6 - updated rows=" + rows);
    }

    // update by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      Account example = new Account();
      example.setCreatedOn(creationDate);
      Account updateValues = new Account();
      updateValues.setCurrentBalance(0);
      int rows = this.accountDao.updateByExample(example, updateValues);
      System.out.println("Test #7 - updated rows=" + rows);
    }

    // delete

    {
      Account a = new Account();
      a.setId(104);
      int rows = this.accountDao.delete(a);
      System.out.println("Test #8 - deleted rows=" + rows);
    }

    // delete by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      Account example = new Account();
      example.setCreatedOn(creationDate);
      int rows = this.accountDao.deleteByExample(example);
      System.out.println("Test #9 - updated rows=" + rows);
    }

    // Views

    // {
    // TxBranchDAO tdao = SpringBeanRetriever.getBean("txBranchDAO");
    // TxBranch example = new TxBranch();
    // example.setBranchId(681);
    // List<TxBranch> txs = tdao.selectByExample(example,
    // TxBranchOrderBy.ACCOUNT_ID, TxBranchOrderBy.AMOUNT);
    // for (TxBranch b : txs) {
    // System.out.println("Test #10: b=" + b);
    // }
    // }

    // Selects

  }

}
