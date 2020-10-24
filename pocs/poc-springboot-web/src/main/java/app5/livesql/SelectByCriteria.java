package app5.livesql;

import java.util.List;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrodorm.hotrod.utils.HexaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app5.persistence.AccountVO;
import app5.persistence.TypesBinaryVO;
import app5.persistence.TypesOtherVO;
import app5.persistence.primitives.AccountDAO;
import app5.persistence.primitives.AccountDAO.AccountTable;
import app5.persistence.primitives.TransactionDAO;
import app5.persistence.primitives.TransactionDAO.TransactionTable;
import app5.persistence.primitives.TypesBinaryDAO;
import app5.persistence.primitives.TypesBinaryDAO.TypesBinaryTable;
import app5.persistence.primitives.TypesOtherDAO;
import app5.persistence.primitives.TypesOtherDAO.TypesOtherTable;

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

  public void runSelectbyCriteria() {

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

  public void runSelectbyCriteriaBinary() {
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

  public void runSelectbyCriteriaUUID() {
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

  public void runSelectbyCriteriaIn() {
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
