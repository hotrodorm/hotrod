// Autogenerated by HotRod -- Do not edit.

package persistence.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import org.hotrod.runtime.tx.TxManager;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import persistence.Cuenta;
import persistence.Cliente;
import persistence.primitives.ClienteDAO;


public class CuentaDAO implements Serializable {

  private static final long serialVersionUID = 1L;

  private SqlSession sqlSession;

  // Bean setter

  public void setSqlSession(final SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }


  // select by primary key

  public Cuenta selectByPK(final java.lang.String numCta) {
    if (numCta == null)
      return null;
    Cuenta vo = new Cuenta();
    vo.setNumCta(numCta);
    return this.sqlSession.selectOne("persistence.primitives.cuenta.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example (with ordering)

  public List<Cuenta> selectByExample(final Cuenta example, final CuentaOrderBy... orderBies)
      {
    DaoWithOrder<Cuenta, CuentaOrderBy> dwo = //
        new DaoWithOrder<Cuenta, CuentaOrderBy>(example, orderBies);
    return this.sqlSession.selectList("persistence.primitives.cuenta.selectByExample", dwo);
  }

  // insert

  public int insert(final Cuenta vo) {
    String id = "persistence.primitives.cuenta.insert";
    return this.sqlSession.insert(id, vo);
  }

  // update by PK

  public int update(final Cuenta vo) {
    if (vo.numCta == null) return 0;
    return this.sqlSession.update("persistence.primitives.cuenta.updateByPK", vo);
  }

  // delete by PK

  public int delete(final Cuenta vo) {
    if (vo.numCta == null) return 0;
    return this.sqlSession.delete("persistence.primitives.cuenta.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final Cuenta example, final Cuenta updateValues) {
    UpdateByExampleDao<Cuenta> fvd = //
      new UpdateByExampleDao<Cuenta>(example, updateValues);
    return this.sqlSession.update("persistence.primitives.cuenta.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final Cuenta example) {
    return this.sqlSession.delete("persistence.primitives.cuenta.deleteByExample", example);
  }

  // DAO ordering

  public enum CuentaOrderBy implements OrderBy {

    NUM_CTA("cuenta", "num_cta", true), //
    NUM_CTA$DESC("cuenta", "num_cta", false), //
    NUM_CTA$CASEINSENSITIVE("cuenta", "lower(num_cta)", true), //
    NUM_CTA$CASEINSENSITIVE_STABLE_FORWARD("cuenta", "lower(num_cta), num_cta", true), //
    NUM_CTA$CASEINSENSITIVE_STABLE_REVERSE("cuenta", "lower(num_cta), num_cta", false), //
    NUM_CTA$DESC_CASEINSENSITIVE("cuenta", "lower(num_cta)", false), //
    NUM_CTA$DESC_CASEINSENSITIVE_STABLE_FORWARD("cuenta", "lower(num_cta), num_cta", false), //
    NUM_CTA$DESC_CASEINSENSITIVE_STABLE_REVERSE("cuenta", "lower(num_cta), num_cta", true), //
    ID_CLIENTE("cuenta", "id_cliente", true), //
    ID_CLIENTE$DESC("cuenta", "id_cliente", false), //
    SALDO("cuenta", "saldo", true), //
    SALDO$DESC("cuenta", "saldo", false), //
    CREADA_EN("cuenta", "creada_en", true), //
    CREADA_EN$DESC("cuenta", "creada_en", false);

    private CuentaOrderBy(final String tableName, final String columnName,
        boolean ascending) {
      this.tableName = tableName;
      this.columnName = columnName;
      this.ascending = ascending;
    }

    private String tableName;
    private String columnName;
    private boolean ascending;

    public String getTableName() {
      return this.tableName;
    }

    public String getColumnName() {
      return this.columnName;
    }

    public boolean isAscending() {
      return this.ascending;
    }

  }

}
