package examples.livesql;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.util.HexaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.TypesBinaryVO;
import hotrod.test.generation.TypesOtherVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;
import hotrod.test.generation.primitives.TransactionDAO;
import hotrod.test.generation.primitives.TransactionDAO.TransactionTable;
import hotrod.test.generation.primitives.TypesBinaryDAO;
import hotrod.test.generation.primitives.TypesBinaryDAO.TypesBinaryTable;
import hotrod.test.generation.primitives.TypesOtherDAO;
import hotrod.test.generation.primitives.TypesOtherDAO.TypesOtherTable;

@Component("selectByCriteria")
public class SelectByCriteria {

  @Autowired
  private AccountDAO accountDao;

  @Autowired
  private TypesBinaryDAO typesBinaryDao;

  @Autowired
  private TypesOtherDAO typesOtherDao;

  @Autowired
  private LiveSQL sql;

  public void runSelectbyCriteria() throws SQLException {

    // SELECT *
    // FROM account
    // WHERE current_balance > 100 and name like 'CHK%'

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

}
