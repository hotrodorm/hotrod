// Autogenerated by HotRod -- Do not edit.

package test.persistence.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import test.persistence.primitives.EmployeeState;
import test.persistence.EmployeeStateVO;
import test.persistence.EmployeeVO;
import test.persistence.primitives.EmployeeDAO.EmployeeOrderBy;
import test.persistence.primitives.EmployeeDAO;

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
public class EmployeeStateDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Lazy
  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select by primary key

  public test.persistence.EmployeeStateVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    test.persistence.EmployeeStateVO vo = new test.persistence.EmployeeStateVO();
    vo.setId(id);
    return this.sqlSession.selectOne("test.persistence.primitives.employeeState.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<test.persistence.EmployeeStateVO> selectByExample(final test.persistence.primitives.EmployeeState example, final EmployeeStateOrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.EmployeeState, EmployeeStateOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("test.persistence.primitives.employeeState.selectByExample", dwo);
  }

  public Cursor<test.persistence.EmployeeStateVO> selectByExampleCursor(final test.persistence.primitives.EmployeeState example, final EmployeeStateOrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.EmployeeState, EmployeeStateOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<test.persistence.EmployeeStateVO>(this.sqlSession.selectCursor("test.persistence.primitives.employeeState.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<test.persistence.EmployeeStateVO> selectByCriteria(final EmployeeStateDAO.EmployeeStateTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<test.persistence.EmployeeStateVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "test.persistence.primitives.employeeState.selectByCriteria");
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs

  public SelectChildrenEmployeePhase selectChildrenEmployeeOf(final EmployeeStateVO vo) {
    return new SelectChildrenEmployeePhase(vo);
  }

  public class SelectChildrenEmployeePhase {

    private EmployeeStateVO vo;

    SelectChildrenEmployeePhase(final EmployeeStateVO vo) {
      this.vo = vo;
    }

    public SelectChildrenEmployeeFromIdPhase fromId() {
      return new SelectChildrenEmployeeFromIdPhase(this.vo);
    }

  }

  public class SelectChildrenEmployeeFromIdPhase {

    private EmployeeStateVO vo;

    SelectChildrenEmployeeFromIdPhase(final EmployeeStateVO vo) {
      this.vo = vo;
    }

    public List<EmployeeVO> toStateId(final EmployeeOrderBy... orderBies) {
      EmployeeVO example = new EmployeeVO();
      example.setStateId(this.vo.getId());
      return employeeDAO.selectByExample(example, orderBies);
    }

    public Cursor<EmployeeVO> cursorToStateId(final EmployeeOrderBy... orderBies) {
      EmployeeVO example = new EmployeeVO();
      example.setStateId(this.vo.getId());
      return employeeDAO.selectByExampleCursor(example, orderBies);
    }

  }

  // insert

  public test.persistence.EmployeeStateVO insert(final test.persistence.primitives.EmployeeState vo) {
    String id = "test.persistence.primitives.employeeState.insert";
    this.sqlSession.insert(id, vo);
    test.persistence.EmployeeStateVO mo = new test.persistence.EmployeeStateVO();
    mo.setId(vo.getId());
    mo.setDescription(vo.getDescription());
    return mo;
  }

  // update by PK

  public int update(final test.persistence.EmployeeStateVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("test.persistence.primitives.employeeState.updateByPK", vo);
  }

  // delete by PK

  public int delete(final test.persistence.EmployeeStateVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("test.persistence.primitives.employeeState.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final test.persistence.primitives.EmployeeState example, final test.persistence.primitives.EmployeeState updateValues) {
    UpdateByExampleDao<test.persistence.primitives.EmployeeState> fvd = //
      new UpdateByExampleDao<test.persistence.primitives.EmployeeState>(example, updateValues);
    return this.sqlSession.update("test.persistence.primitives.employeeState.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final test.persistence.primitives.EmployeeState example) {
    return this.sqlSession.delete("test.persistence.primitives.employeeState.deleteByExample", example);
  }

  // DAO ordering

  public enum EmployeeStateOrderBy implements OrderBy {

    ID("employee_state", "id", true), //
    ID$DESC("employee_state", "id", false), //
    DESCRIPTION("employee_state", "description", true), //
    DESCRIPTION$DESC("employee_state", "description", false), //
    DESCRIPTION$CASEINSENSITIVE("employee_state", "lower(description)", true), //
    DESCRIPTION$CASEINSENSITIVE_STABLE_FORWARD("employee_state", "lower(description), description", true), //
    DESCRIPTION$CASEINSENSITIVE_STABLE_REVERSE("employee_state", "lower(description), description", false), //
    DESCRIPTION$DESC_CASEINSENSITIVE("employee_state", "lower(description)", false), //
    DESCRIPTION$DESC_CASEINSENSITIVE_STABLE_FORWARD("employee_state", "lower(description), description", false), //
    DESCRIPTION$DESC_CASEINSENSITIVE_STABLE_REVERSE("employee_state", "lower(description), description", true);

    private EmployeeStateOrderBy(final String tableName, final String columnName,
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

  public static EmployeeStateTable newTable() {
    return new EmployeeStateTable();
  }

  public static EmployeeStateTable newTable(final String alias) {
    return new EmployeeStateTable(alias);
  }

  public static class EmployeeStateTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn description;

    // Constructors

    EmployeeStateTable() {
      super(null, null, "employee_state", "Table", null);
      initialize();
    }

    EmployeeStateTable(final String alias) {
      super(null, null, "employee_state", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.description = new StringColumn(this, "description", "description");
    }

  }

}