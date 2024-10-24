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

import test.persistence.primitives.Island;
import test.persistence.IslandVO;

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
public class IslandDAO implements Serializable, ApplicationContextAware {

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

  public List<test.persistence.IslandVO> selectByExample(final test.persistence.primitives.Island example, final IslandOrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.Island, IslandOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("test.persistence.primitives.island.selectByExample", dwo);
  }

  public Cursor<test.persistence.IslandVO> selectByExampleCursor(final test.persistence.primitives.Island example, final IslandOrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.Island, IslandOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<test.persistence.IslandVO>(this.sqlSession.selectCursor("test.persistence.primitives.island.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<test.persistence.IslandVO> selectByCriteria(final IslandDAO.IslandTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<test.persistence.IslandVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "test.persistence.primitives.island.selectByCriteria");
  }

  // select parent(s) by FKs: no imported keys found -- skipped

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public test.persistence.IslandVO insert(final test.persistence.primitives.Island vo) {
    String id = "test.persistence.primitives.island.insert";
    this.sqlSession.insert(id, vo);
    test.persistence.IslandVO mo = new test.persistence.IslandVO();
    mo.setId(vo.getId());
    mo.setSegment(vo.getSegment());
    mo.setXStart(vo.getXStart());
    mo.setXEnd(vo.getXEnd());
    mo.setHeight(vo.getHeight());
    return mo;
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int updateByExample(final test.persistence.primitives.Island example, final test.persistence.primitives.Island updateValues) {
    UpdateByExampleDao<test.persistence.primitives.Island> fvd = //
      new UpdateByExampleDao<test.persistence.primitives.Island>(example, updateValues);
    return this.sqlSession.update("test.persistence.primitives.island.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final test.persistence.primitives.Island example) {
    return this.sqlSession.delete("test.persistence.primitives.island.deleteByExample", example);
  }

  // DAO ordering

  public enum IslandOrderBy implements OrderBy {

    ID("island", "id", true), //
    ID$DESC("island", "id", false), //
    SEGMENT("island", "segment", true), //
    SEGMENT$DESC("island", "segment", false), //
    X_START("island", "x_start", true), //
    X_START$DESC("island", "x_start", false), //
    X_END("island", "x_end", true), //
    X_END$DESC("island", "x_end", false), //
    HEIGHT("island", "height", true), //
    HEIGHT$DESC("island", "height", false);

    private IslandOrderBy(final String tableName, final String columnName,
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

  public static IslandTable newTable() {
    return new IslandTable();
  }

  public static IslandTable newTable(final String alias) {
    return new IslandTable(alias);
  }

  public static class IslandTable extends Table {

    // Properties

    public NumberColumn id;
    public NumberColumn segment;
    public NumberColumn xStart;
    public NumberColumn xEnd;
    public NumberColumn height;

    // Constructors

    IslandTable() {
      super(null, null, "island", "Table", null);
      initialize();
    }

    IslandTable(final String alias) {
      super(null, null, "island", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.segment = new NumberColumn(this, "segment", "segment");
      this.xStart = new NumberColumn(this, "x_start", "xStart");
      this.xEnd = new NumberColumn(this, "x_end", "xEnd");
      this.height = new NumberColumn(this, "height", "height");
    }

  }

}
