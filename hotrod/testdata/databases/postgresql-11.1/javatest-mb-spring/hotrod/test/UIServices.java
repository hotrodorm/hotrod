package hotrod.test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.hotrod.runtime.util.HexaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.AccountVolumeVO;
import hotrod.test.generation.TypesBinaryVO;
import hotrod.test.generation.TypesOtherVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountOrderBy;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;
import hotrod.test.generation.primitives.AccountReportsDAO;
import hotrod.test.generation.primitives.ClientDAO;
import hotrod.test.generation.primitives.ClientDAO.ClientTable;
import hotrod.test.generation.primitives.TransactionDAO;
import hotrod.test.generation.primitives.TransactionDAO.TransactionTable;
import hotrod.test.generation.primitives.TypesBinaryDAO;
import hotrod.test.generation.primitives.TypesBinaryDAO.TypesBinaryTable;
import hotrod.test.generation.primitives.TypesOtherDAO;
import hotrod.test.generation.primitives.TypesOtherDAO.TypesOtherTable;

@Component("uiServices")
public class UIServices {

  @Autowired
  private AccountDAO accountDao;

  @Autowired
  private TypesBinaryDAO typesBinaryDao;

  @Autowired
  private TypesOtherDAO typesOtherDao;

  @Autowired
  private AccountReportsDAO accountReportsDAO;

  @Autowired
  private LiveSQL sql;

  public void runLiveSQL1() throws SQLException {

    AccountTable a = AccountDAO.newTable();
    ClientTable c = ClientDAO.newTable("c");

    List<Map<String, Object>> rows = this.sql //
        .select(a.createdOn,

            a.name.isNull(),

            sql.sum(a.currentBalance).over().partitionBy(a.type).orderBy(a.name.substr(5, 2).asc()).end()
                .as("runningBalance"),

            a.currentBalance.mult(a.id).lt(10),

            sql.val(1.10).mult(a.currentBalance).as("cbp"), //
            a.currentBalance.trunc(-2).as("tb"), //
            a.createdOn.extract(DateTimeField.MONTH).as("month"), //
            a.name.coalesce(a.type).coalesce("N/A").as("xname"), //
            sql.val("%").concat(a.name).concat(a.type).concat("%").as("like1"), //
            a.currentBalance, a.mainStatus, a.id, a.type) //
        .from(a) //
        .where(a.mainStatus.eq(1) //
            .and(a.currentBalance.lt(100)) //
            .or(a.type.ne("S'AV") //
                .and(sql.exists(this.sql.select().from(c).where(c.friendId.isNotNull())))) //
            .andNot(a.mainStatus.ne(14).or(a.createdOn.gt(sql.currentDateTime())))) //
        .orderBy(a.createdOn.asc()) //
        .execute();

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

  }

  public void runLiveSQL2() throws SQLException {

    AccountTable a = AccountDAO.newTable("a");
    AccountTable b = AccountDAO.newTable("b");
    // ClientTable c = ClientDAO.newTable("c");

    List<Map<String, Object>> rows = this.sql //
        .select(a.name, a.currentBalance) //
        .from(a) //
        // .unionAll(this.sql.select(b.name,
        // b.currentBalance).from(b).where(b.name.like("CHK%"))) //
        // .orderBy(a.currentBalance.asc())
        .limit(2) //
        .execute();

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

  }

  public void runLiveSQL3() throws SQLException {

    AccountTable a = AccountDAO.newTable("a");
    AccountTable b = AccountDAO.newTable("b");
    // ClientTable c = ClientDAO.newTable("c");

    // List<Map<String, Object>> rows =
    // this.clientDao.encloseSelect(this.clientDao //
    // .createSelect(a.name, a.currentBalance) //
    // .from(a) //
    // .unionAll(this.clientDao.createSelect(b.name,
    // b.currentBalance).from(b).where(b.name.like("CHK%"))) //
    // // .orderBy(a.currentBalance.asc())
    // )
    //
    // .
    // .limit(2) //
    // .execute()
    // ;
    //
    // if (rows != null) {
    // for (Map<String, Object> r : rows) {
    // System.out.println("row: " + r);
    // }
    // }

  }

  public void getNewAccountVolume() {
    List<AccountVolumeVO> v = this.accountReportsDAO.selectNewAccountsVolume();
    for (AccountVolumeVO av : v) {
      System.out.println("av=" + av);
    }

    int rows = this.accountReportsDAO.applyPromotion(150, 100);
    System.out.println("rows=" + rows);
  }

  public void runSelectbyCriteria() throws SQLException {
    AccountTable a = AccountDAO.newTable();
    List<AccountVO> rows = this.accountDao.selectByCriteria(a, //
        a.currentBalance.gt(100) //
            .and(a.name.like("CHK%"))) //
        .execute();
    for (AccountVO r : rows) {
      System.out.println("row: " + r);
    }
  }

  public void runSelectbyCriteriaBinary() throws SQLException {
    byte[] searched = new byte[2];
    searched[0] = 0x31;
    searched[1] = 0x35;
    TypesBinaryTable b = TypesBinaryDAO.newTable();
    List<TypesBinaryVO> rows = this.typesBinaryDao.selectByCriteria(b, b.bin1.eq(searched)) //
        .execute();
    for (TypesBinaryVO r : rows) {
      System.out.println("row: [" + r.getBol1() + ", " + HexaUtils.toHexa(r.getBin1()) + "]");
    }
  }

  public void runSelectbyCriteriaUUID() throws SQLException {
    byte[] searched = new byte[2];
    searched[0] = 0x31;
    searched[1] = 0x35;
    TypesOtherTable b = TypesOtherDAO.newTable();
    List<TypesOtherVO> rows = this.typesOtherDao.selectByCriteria(b, b.uui1.ne("33bb9554-c616-42e6-a9c6-88d3bba4221c")) //
        .execute();

    for (TypesOtherVO r : rows) {
      System.out.println("row: [" + r.toJSON() + "]");
    }
  }

  public void runSelectbyCriteriaIn() throws SQLException {
    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");
    List<AccountVO> rows = this.accountDao.selectByCriteria(a, a.id.in( //
        this.sql.select(t.accountId) //
            .from(t) //
            .where(t.accountId.eq(a.id).andNot(t.amount.ge(100).or(t.time.isNull())))) //
        .and(a.id.notIn(123, 456, 789))) //
        .execute();
    for (AccountVO r : rows) {
      System.out.println("row: " + r);
    }
  }

  public void runExamples() throws SQLException {

    // Select by PK

    {
      AccountVO a = this.accountDao.selectByPK(1004);
      System.out.println("Test #1: a=" + a);
    }

    // Select by UI

    {
      AccountVO a = this.accountDao.selectByUIName("CHK1004");
      System.out.println("Test #2: a=" + a);
      System.out.println("Test #2 JSON: a=" + (a == null ? "null" : a.toJSON()));
    }

    // Select by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      List<AccountVO> accounts = this.accountDao.selectByExample(example);
      if (accounts.isEmpty()) {
        System.out.println("Test #3: --- No accounts found.");
      } else {
        for (AccountVO a : accounts) {
          System.out.println("Test #3: a=" + a);
        }
      }
    }

    // Select by example with order by

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      List<AccountVO> accounts = this.accountDao.selectByExample(example, AccountOrderBy.CURRENT_BALANCE$DESC,
          AccountOrderBy.NAME);
      if (accounts.isEmpty()) {
        System.out.println("Test #4: --- No accounts found.");
      } else {
        for (AccountVO a : accounts) {
          System.out.println("Test #4: a=" + a);
        }
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
      // a.setActive(1);
      this.accountDao.insert(a);
      System.out.println("Test #5 - new account id is: " + a.getId());
    }

    // update the balance

    {
      AccountVO a = this.accountDao.selectByPK(1234004);
      a.setCurrentBalance(250);
      int rows = this.accountDao.update(a);
      System.out.println("Test #6 - updated rows=" + rows);
    }

    // update by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
      example.setCreatedOn(creationDate);
      AccountVO updateValues = new AccountVO();
      updateValues.setCurrentBalance(0);
      int rows = this.accountDao.updateByExample(example, updateValues);
      System.out.println("Test #7 - updated rows=" + rows);
    }

    // delete

    {
      AccountVO a = new AccountVO();
      a.setId(104);
      int rows = this.accountDao.delete(a);
      System.out.println("Test #8 - deleted rows=" + rows);
    }

    // delete by example

    {
      Timestamp creationDate = new Timestamp(System.currentTimeMillis());

      AccountVO example = new AccountVO();
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
