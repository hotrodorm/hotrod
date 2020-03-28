package com.myapp1.generado.daos;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.OrderBy;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;

import com.myapp1.generado.vos.CuentaVO;

public class CuentaDAO {

  // Beans

  private SqlSession sqlSession;

  // Bean setter

  public void setSqlSession(final SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  // @Transactional(propagation = Propagation.REQUIRED)
  // public List<CuentaVO> leerTodasLasCuentas() {
  // return this.sqlSession.selectList("com.myapp1.cuenta.selectAll");
  // }

  // ====================================================

  // select by primary key

  public CuentaVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    CuentaVO pk = new CuentaVO();
    pk.setId(id);
    return this.sqlSession.selectOne("com.myapp1.cuenta.selectByPK", pk);
  }

  // select by unique indexes: no unique indexes found (besides the PK) --
  // skipped

  // select by example (with ordering)

  public List<CuentaVO> selectByExample(final CuentaVO example, final CuentaOrderBy... orderBies) {
    DaoWithOrder<CuentaVO, CuentaOrderBy> dwo = //
        new DaoWithOrder<CuentaVO, CuentaOrderBy>(example, orderBies);
    return this.sqlSession.selectList("com.myapp1.cuenta.selectByExample", dwo);
  }

  // select parents by imported FKs: no imported keys found -- skipped

  // select children by exported FKs: no exported keys found -- skipped

  // insert

  public int insert(final CuentaVO vo) {
    return this.sqlSession.insert("com.myapp1.cuenta.insert", vo);
  }

  // update by PK

  public int update(final CuentaVO vo) {
    if (vo.getId() == null) {
      return 0;
    }
    return this.sqlSession.update("com.myapp1.cuenta.updateByPK", vo);
  }

  // delete by PK

  public int delete(final CuentaVO vo) {
    if (vo.getId() == null) {
      return 0;
    }
    return sqlSession.delete("com.myapp1.cuenta.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final CuentaVO example, final CuentaVO updateValues) {
    UpdateByExampleDao<CuentaVO> fvd = //
        new UpdateByExampleDao<CuentaVO>(example, updateValues);
    return sqlSession.update("com.myapp1.cuenta.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final CuentaVO example) {
    return this.sqlSession.delete("com.myapp1.cuenta.deleteByExample", example);
  }

  // DAO ordering

  public enum CuentaOrderBy implements OrderBy {

    ID("CUENTA", "ID", true), //
    ID$DESC("CUENTA", "ID", false), //
    NUMERO("CUENTA", "NUMERO", true), //
    NUMERO$DESC("CUENTA", "NUMERO", false), //
    NUMERO$CASEINSENSITIVE("CUENTA", "lower(NUMERO)", true), //
    NUMERO$CASEINSENSITIVE_STABLE_FORWARD("CUENTA", "lower(NUMERO), NUMERO", true), //
    NUMERO$CASEINSENSITIVE_STABLE_REVERSE("CUENTA", "lower(NUMERO), NUMERO", false), //
    NUMERO$DESC_CASEINSENSITIVE("CUENTA", "lower(NUMERO)", false), //
    NUMERO$DESC_CASEINSENSITIVE_STABLE_FORWARD("CUENTA", "lower(NUMERO), NUMERO", false), //
    NUMERO$DESC_CASEINSENSITIVE_STABLE_REVERSE("CUENTA", "lower(NUMERO), NUMERO", true), //
    TIPO("CUENTA", "TIPO", true), //
    TIPO$DESC("CUENTA", "TIPO", false), //
    TIPO$CASEINSENSITIVE("CUENTA", "lower(TIPO)", true), //
    TIPO$CASEINSENSITIVE_STABLE_FORWARD("CUENTA", "lower(TIPO), TIPO", true), //
    TIPO$CASEINSENSITIVE_STABLE_REVERSE("CUENTA", "lower(TIPO), TIPO", false), //
    TIPO$DESC_CASEINSENSITIVE("CUENTA", "lower(TIPO)", false), //
    TIPO$DESC_CASEINSENSITIVE_STABLE_FORWARD("CUENTA", "lower(TIPO), TIPO", false), //
    TIPO$DESC_CASEINSENSITIVE_STABLE_REVERSE("CUENTA", "lower(TIPO), TIPO", true), //
    SALDO("CUENTA", "SALDO", true), //
    SALDO$DESC("CUENTA", "SALDO", false), //
    CREADA_EN("CUENTA", "CREADA_EN", true), //
    CREADA_EN$DESC("CUENTA", "CREADA_EN", false);

    private CuentaOrderBy(final String tableName, final String columnName, boolean ascending) {
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
