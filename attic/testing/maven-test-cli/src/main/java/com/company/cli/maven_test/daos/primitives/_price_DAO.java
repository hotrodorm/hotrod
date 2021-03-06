// Autogenerated by HotRod -- Do not edit.

package com.company.cli.maven_test.daos.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import com.company.cli.maven_test.daos._price_VO;

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

@Component("_price_DAO")
public class _price_DAO implements Serializable, ApplicationContextAware {

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

  public List<com.company.cli.maven_test.daos._price_VO> selectByExample(final com.company.cli.maven_test.daos._price_VO example, final _price_OrderBy... orderBies)
      {
    DaoWithOrder<com.company.cli.maven_test.daos._price_VO, _price_OrderBy> dwo = //
        new DaoWithOrder<com.company.cli.maven_test.daos._price_VO, _price_OrderBy>(example, orderBies);
    return this.sqlSession.selectList("com.company.cli.maven_test.daos.primitives._price_.selectByExample", dwo);
  }

  // select by criteria

  public CriteriaWherePhase<com.company.cli.maven_test.daos._price_VO> selectByCriteria(final _price_DAO._price_Table from,
      final Predicate predicate) {
    return new CriteriaWherePhase<com.company.cli.maven_test.daos._price_VO>(from, this.sqlDialect, this.sqlSession,
        predicate, "com.company.cli.maven_test.daos.primitives._price_.selectByCriteria");
  }

  // insert

  public int insert(final com.company.cli.maven_test.daos._price_VO vo) {
    String id = "com.company.cli.maven_test.daos.primitives._price_.insert";
    int rows = this.sqlSession.insert(id, vo);
    return rows;
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int updateByExample(final com.company.cli.maven_test.daos._price_VO example, final com.company.cli.maven_test.daos._price_VO updateValues) {
    UpdateByExampleDao<com.company.cli.maven_test.daos._price_VO> fvd = //
      new UpdateByExampleDao<com.company.cli.maven_test.daos._price_VO>(example, updateValues);
    return this.sqlSession.update("com.company.cli.maven_test.daos.primitives._price_.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final com.company.cli.maven_test.daos._price_VO example) {
    return this.sqlSession.delete("com.company.cli.maven_test.daos.primitives._price_.deleteByExample", example);
  }

  // DAO ordering

  public enum _price_OrderBy implements OrderBy {

    ID("\"<Stock$\".\"&Price%\"", "id", true), //
    ID$DESC("\"<Stock$\".\"&Price%\"", "id", false), //
    VALUE("\"<Stock$\".\"&Price%\"", "value", true), //
    VALUE$DESC("\"<Stock$\".\"&Price%\"", "value", false);

    private _price_OrderBy(final String tableName, final String columnName,
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

  public static _price_Table newTable() {
    return new _price_Table();
  }

  public static _price_Table newTable(final String alias) {
    return new _price_Table(alias);
  }

  public static class _price_Table extends Table {

    // Properties

    public NumberColumn id;
    public NumberColumn value;

    // Constructors

    _price_Table() {
      super(null, "<Stock$", "&Price%", "Table", null);
      initialize();
    }

    _price_Table(final String alias) {
      super(null, "<Stock$", "&Price%", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.value = new NumberColumn(this, "value", "value");
    }

  }

}
