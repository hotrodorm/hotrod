// Autogenerated by HotRod -- Do not edit.

package com.company.daos.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import com.company.daos.IslandVO;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component("islandDAO")
public class IslandDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Value("#{sqlDialectFactory.sqlDialect}")
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // no select by PK generated, since the table does not have a PK.

  // select by unique indexes: no unique indexes found -- skipped

  // select by example

  public List<com.company.daos.IslandVO> selectByExample(final com.company.daos.IslandVO example, final IslandOrderBy... orderBies)
      {
    DaoWithOrder<com.company.daos.IslandVO, IslandOrderBy> dwo = //
        new DaoWithOrder<com.company.daos.IslandVO, IslandOrderBy>(example, orderBies);
    return this.sqlSession.selectList("com.company.daos.primitives.island.selectByExample", dwo);
  }

  // select by criteria

  public CriteriaWherePhase<com.company.daos.IslandVO> selectByCriteria(final IslandDAO.IslandTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<com.company.daos.IslandVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "com.company.daos.primitives.island.selectByCriteria");
  }

  // insert

  public int insert(final com.company.daos.IslandVO vo) {
    String id = "com.company.daos.primitives.island.insert";
    return this.sqlSession.insert(id, vo);
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int updateByExample(final com.company.daos.IslandVO example, final com.company.daos.IslandVO updateValues) {
    UpdateByExampleDao<com.company.daos.IslandVO> fvd = //
      new UpdateByExampleDao<com.company.daos.IslandVO>(example, updateValues);
    return this.sqlSession.update("com.company.daos.primitives.island.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final com.company.daos.IslandVO example) {
    return this.sqlSession.delete("com.company.daos.primitives.island.deleteByExample", example);
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