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

import test.persistence.primitives.TestDefault2;
import test.persistence.TestDefault2VO;

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
public class TestDefault2DAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Autowired
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // no select by PK generated, since the table does not have a PK.

  // select by unique indexes: no unique indexes found -- skipped

  // select by example

  public List<test.persistence.TestDefault2VO> selectByExample(final test.persistence.primitives.TestDefault2 example, final TestDefault2OrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.TestDefault2, TestDefault2OrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("test.persistence.primitives.testDefault2.selectByExample", dwo);
  }

  public Cursor<test.persistence.TestDefault2VO> selectByExampleCursor(final test.persistence.primitives.TestDefault2 example, final TestDefault2OrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.TestDefault2, TestDefault2OrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<test.persistence.TestDefault2VO>(this.sqlSession.selectCursor("test.persistence.primitives.testDefault2.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<test.persistence.TestDefault2VO> selectByCriteria(final TestDefault2DAO.TestDefault2Table from,
      final Predicate predicate) {
    return new CriteriaWherePhase<test.persistence.TestDefault2VO>(from, this.sqlDialect, this.sqlSession,
        predicate, "test.persistence.primitives.testDefault2.selectByCriteria");
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public test.persistence.TestDefault2VO insert(final test.persistence.primitives.TestDefault2 vo) {
    return insert(vo, false);
  }

  public test.persistence.TestDefault2VO insert(final test.persistence.primitives.TestDefault2 vo, final boolean retrieveDefaults) {
    String id = retrieveDefaults ? "test.persistence.primitives.testDefault2.insertRetrievingDefaults" : "test.persistence.primitives.testDefault2.insert";
    this.sqlSession.insert(id, vo);
    test.persistence.TestDefault2VO mo = new test.persistence.TestDefault2VO();
    mo.setName(vo.getName());
    mo.setPrice(vo.getPrice());
    mo.setBranchId(vo.getBranchId());
    return mo;
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int updateByExample(final test.persistence.primitives.TestDefault2 example, final test.persistence.primitives.TestDefault2 updateValues) {
    UpdateByExampleDao<test.persistence.primitives.TestDefault2> fvd = //
      new UpdateByExampleDao<test.persistence.primitives.TestDefault2>(example, updateValues);
    return this.sqlSession.update("test.persistence.primitives.testDefault2.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final test.persistence.primitives.TestDefault2 example) {
    return this.sqlSession.delete("test.persistence.primitives.testDefault2.deleteByExample", example);
  }

  // DAO ordering

  public enum TestDefault2OrderBy implements OrderBy {

    NAME("test_default2", "name", true), //
    NAME$DESC("test_default2", "name", false), //
    NAME$CASEINSENSITIVE("test_default2", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("test_default2", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("test_default2", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("test_default2", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("test_default2", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("test_default2", "lower(name), name", true), //
    PRICE("test_default2", "price", true), //
    PRICE$DESC("test_default2", "price", false), //
    BRANCH_ID("test_default2", "branch_id", true), //
    BRANCH_ID$DESC("test_default2", "branch_id", false);

    private TestDefault2OrderBy(final String tableName, final String columnName,
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

  public static TestDefault2Table newTable() {
    return new TestDefault2Table();
  }

  public static TestDefault2Table newTable(final String alias) {
    return new TestDefault2Table(alias);
  }

  public static class TestDefault2Table extends Table {

    // Properties

    public StringColumn name;
    public NumberColumn price;
    public NumberColumn branchId;

    // Constructors

    TestDefault2Table() {
      super(null, null, "test_default2", "Table", null);
      initialize();
    }

    TestDefault2Table(final String alias) {
      super(null, null, "test_default2", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.name = new StringColumn(this, "name", "name");
      this.price = new NumberColumn(this, "price", "price");
      this.branchId = new NumberColumn(this, "branch_id", "branchId");
    }

  }

}
