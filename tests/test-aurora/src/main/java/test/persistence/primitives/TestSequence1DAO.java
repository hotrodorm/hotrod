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

import test.persistence.primitives.TestSequence1;
import test.persistence.TestSequence1VO;

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
public class TestSequence1DAO implements Serializable, ApplicationContextAware {

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

  public List<test.persistence.TestSequence1VO> selectByExample(final test.persistence.primitives.TestSequence1 example, final TestSequence1OrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.TestSequence1, TestSequence1OrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("test.persistence.primitives.testSequence1.selectByExample", dwo);
  }

  public Cursor<test.persistence.TestSequence1VO> selectByExampleCursor(final test.persistence.primitives.TestSequence1 example, final TestSequence1OrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.TestSequence1, TestSequence1OrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<test.persistence.TestSequence1VO>(this.sqlSession.selectCursor("test.persistence.primitives.testSequence1.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<test.persistence.TestSequence1VO> selectByCriteria(final TestSequence1DAO.TestSequence1Table from,
      final Predicate predicate) {
    return new CriteriaWherePhase<test.persistence.TestSequence1VO>(from, this.sqlDialect, this.sqlSession,
        predicate, "test.persistence.primitives.testSequence1.selectByCriteria");
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public test.persistence.TestSequence1VO insert(final test.persistence.primitives.TestSequence1 vo) {
    String id = "test.persistence.primitives.testSequence1.insert";
    this.sqlSession.insert(id, vo);
    test.persistence.TestSequence1VO mo = new test.persistence.TestSequence1VO();
    mo.setId1(vo.getId1());
    mo.setName(vo.getName());
    return mo;
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int updateByExample(final test.persistence.primitives.TestSequence1 example, final test.persistence.primitives.TestSequence1 updateValues) {
    UpdateByExampleDao<test.persistence.primitives.TestSequence1> fvd = //
      new UpdateByExampleDao<test.persistence.primitives.TestSequence1>(example, updateValues);
    return this.sqlSession.update("test.persistence.primitives.testSequence1.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final test.persistence.primitives.TestSequence1 example) {
    return this.sqlSession.delete("test.persistence.primitives.testSequence1.deleteByExample", example);
  }

  // DAO ordering

  public enum TestSequence1OrderBy implements OrderBy {

    ID1("test_sequence1", "id1", true), //
    ID1$DESC("test_sequence1", "id1", false), //
    NAME("test_sequence1", "name", true), //
    NAME$DESC("test_sequence1", "name", false), //
    NAME$CASEINSENSITIVE("test_sequence1", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("test_sequence1", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("test_sequence1", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("test_sequence1", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("test_sequence1", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("test_sequence1", "lower(name), name", true);

    private TestSequence1OrderBy(final String tableName, final String columnName,
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

  public static TestSequence1Table newTable() {
    return new TestSequence1Table();
  }

  public static TestSequence1Table newTable(final String alias) {
    return new TestSequence1Table(alias);
  }

  public static class TestSequence1Table extends Table {

    // Properties

    public NumberColumn id1;
    public StringColumn name;

    // Constructors

    TestSequence1Table() {
      super(null, null, "test_sequence1", "Table", null);
      initialize();
    }

    TestSequence1Table(final String alias) {
      super(null, null, "test_sequence1", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id1 = new NumberColumn(this, "id1", "id1");
      this.name = new StringColumn(this, "name", "name");
    }

  }

}