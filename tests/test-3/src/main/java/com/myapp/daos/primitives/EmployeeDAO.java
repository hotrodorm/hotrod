// Autogenerated by HotRod -- Do not edit.

package com.myapp.daos.primitives;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import com.myapp.daos.primitives.EmployeeVO;
import com.myapp.daos.EmployeeMODEL;
import com.myapp.daos.BranchMODEL;
import com.myapp.daos.primitives.BranchDAO;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeColumn;
import org.hotrod.runtime.livesql.metadata.BooleanColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayColumn;
import org.hotrod.runtime.livesql.metadata.ObjectColumn;
import org.hotrod.runtime.livesql.metadata.Table;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.AllColumns;
import org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase;
import org.hotrod.runtime.livesql.metadata.View;

import org.springframework.stereotype.Component;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component
public class EmployeeDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Lazy
  @Autowired
  private BranchDAO branchDAO;

  @Autowired
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // Row Parser

  public static com.myapp.daos.EmployeeMODEL parseRow(Map<String, Object> m) {
    com.myapp.daos.EmployeeMODEL mo = new com.myapp.daos.EmployeeMODEL();
    mo.setId((java.lang.Integer) m.get("id"));
    mo.setName((java.lang.String) m.get("name"));
    mo.setPhoto((byte[]) m.get("photo"));
    mo.setBio((java.lang.String) m.get("bio"));
    mo.setBranchId((java.lang.Integer) m.get("branchId"));
    return mo;
  }

  // select by primary key

  public com.myapp.daos.EmployeeMODEL selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    com.myapp.daos.EmployeeMODEL vo = new com.myapp.daos.EmployeeMODEL();
    vo.setId(id);
    return this.sqlSession.selectOne("bc.employee.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<com.myapp.daos.EmployeeMODEL> selectByExample(final com.myapp.daos.primitives.EmployeeVO example, final EmployeeOrderBy... orderBies)
      {
    DaoWithOrder<com.myapp.daos.primitives.EmployeeVO, EmployeeOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("bc.employee.selectByExample", dwo);
  }

  public Cursor<com.myapp.daos.EmployeeMODEL> selectByExampleCursor(final com.myapp.daos.primitives.EmployeeVO example, final EmployeeOrderBy... orderBies)
      {
    DaoWithOrder<com.myapp.daos.primitives.EmployeeVO, EmployeeOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<com.myapp.daos.EmployeeMODEL>(this.sqlSession.selectCursor("bc.employee.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<com.myapp.daos.EmployeeMODEL> selectByCriteria(final EmployeeDAO.EmployeeTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<com.myapp.daos.EmployeeMODEL>(from, this.sqlDialect, this.sqlSession,
        predicate, "bc.employee.selectByCriteria");
  }

  // select parent(s) by FKs

  public SelectParentBranchPhase selectParentBranchOf(final EmployeeMODEL vo) {
    return new SelectParentBranchPhase(vo);
  }

  public class SelectParentBranchPhase {

    private EmployeeMODEL vo;

    SelectParentBranchPhase(final EmployeeMODEL vo) {
      this.vo = vo;
    }

    public SelectParentBranchFromBranchIdPhase fromBranchId() {
      return new SelectParentBranchFromBranchIdPhase(this.vo);
    }

  }

  public class SelectParentBranchFromBranchIdPhase {

    private EmployeeMODEL vo;

    SelectParentBranchFromBranchIdPhase(final EmployeeMODEL vo) {
      this.vo = vo;
    }

    public BranchMODEL toBranchId() {
      return branchDAO.selectByPK(this.vo.branchId);
    }

  }

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public com.myapp.daos.EmployeeMODEL insert(final com.myapp.daos.primitives.EmployeeVO vo) {
    String id = "bc.employee.insert";
    int rows = this.sqlSession.insert(id, vo);
    com.myapp.daos.EmployeeMODEL mo = new com.myapp.daos.EmployeeMODEL();
    mo.setId(vo.getId());
    mo.setName(vo.getName());
    mo.setPhoto(vo.getPhoto());
    mo.setBio(vo.getBio());
    mo.setBranchId(vo.getBranchId());
    return mo;
  }

  // update by PK

  public int update(final com.myapp.daos.EmployeeMODEL vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("bc.employee.updateByPK", vo);
  }

  // delete by PK

  public int delete(final com.myapp.daos.EmployeeMODEL vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("bc.employee.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final com.myapp.daos.primitives.EmployeeVO example, final com.myapp.daos.primitives.EmployeeVO updateValues) {
    UpdateByExampleDao<com.myapp.daos.primitives.EmployeeVO> fvd = //
      new UpdateByExampleDao<com.myapp.daos.primitives.EmployeeVO>(example, updateValues);
    return this.sqlSession.update("bc.employee.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final com.myapp.daos.primitives.EmployeeVO example) {
    return this.sqlSession.delete("bc.employee.deleteByExample", example);
  }

  // DAO ordering

  public enum EmployeeOrderBy implements OrderBy {

    ID("employee", "id", true), //
    ID$DESC("employee", "id", false), //
    NAME("employee", "name", true), //
    NAME$DESC("employee", "name", false), //
    NAME$CASEINSENSITIVE("employee", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("employee", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("employee", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("employee", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("employee", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("employee", "lower(name), name", true), //
    PHOTO("employee", "photo", true), //
    PHOTO$DESC("employee", "photo", false), //
    BIO("employee", "bio", true), //
    BIO$DESC("employee", "bio", false), //
    BIO$CASEINSENSITIVE("employee", "lower(bio)", true), //
    BIO$CASEINSENSITIVE_STABLE_FORWARD("employee", "lower(bio), bio", true), //
    BIO$CASEINSENSITIVE_STABLE_REVERSE("employee", "lower(bio), bio", false), //
    BIO$DESC_CASEINSENSITIVE("employee", "lower(bio)", false), //
    BIO$DESC_CASEINSENSITIVE_STABLE_FORWARD("employee", "lower(bio), bio", false), //
    BIO$DESC_CASEINSENSITIVE_STABLE_REVERSE("employee", "lower(bio), bio", true), //
    BRANCH_ID("employee", "branch_id", true), //
    BRANCH_ID$DESC("employee", "branch_id", false);

    private EmployeeOrderBy(final String tableName, final String columnName,
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

  // Database Table metadata

  public static EmployeeTable newTable() {
    return new EmployeeTable();
  }

  public static EmployeeTable newTable(final String alias) {
    return new EmployeeTable(alias);
  }

  public static class EmployeeTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn name;
    public ByteArrayColumn photo;
    public StringColumn bio;
    public NumberColumn branchId;

    // Getters

    public AllColumns star() {
      return new AllColumns(this, this.id, this.name, this.photo, this.bio, this.branchId);
    }

    // Constructors

    EmployeeTable() {
      super(null, null, "EMPLOYEE", "Table", null);
      initialize();
    }

    EmployeeTable(final String alias) {
      super(null, null, "EMPLOYEE", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "ID", "id", "INTEGER", 32, 0);
      this.name = new StringColumn(this, "NAME", "name", "CHARACTER VARYING", 20, 0);
      this.photo = new ByteArrayColumn(this, "PHOTO", "photo", "BINARY LARGE OBJECT", 2147483647, 0);
      this.bio = new StringColumn(this, "BIO", "bio", "CHARACTER LARGE OBJECT", 2147483647, 0);
      this.branchId = new NumberColumn(this, "BRANCH_ID", "branchId", "INTEGER", 32, 0);
    }

  }

}