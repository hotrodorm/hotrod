// Autogenerated by HotRod -- Do not edit.

package com.company.cli.maven_test.daos.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import com.company.cli.maven_test.daos.TypesBinaryVO;

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

@Component("typesBinaryDAO")
public class TypesBinaryDAO implements Serializable, ApplicationContextAware {

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

  public List<com.company.cli.maven_test.daos.TypesBinaryVO> selectByExample(final com.company.cli.maven_test.daos.TypesBinaryVO example, final TypesBinaryOrderBy... orderBies)
      {
    DaoWithOrder<com.company.cli.maven_test.daos.TypesBinaryVO, TypesBinaryOrderBy> dwo = //
        new DaoWithOrder<com.company.cli.maven_test.daos.TypesBinaryVO, TypesBinaryOrderBy>(example, orderBies);
    return this.sqlSession.selectList("com.company.cli.maven_test.daos.primitives.typesBinary.selectByExample", dwo);
  }

  // select by criteria

  public CriteriaWherePhase<com.company.cli.maven_test.daos.TypesBinaryVO> selectByCriteria(final TypesBinaryDAO.TypesBinaryTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<com.company.cli.maven_test.daos.TypesBinaryVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "com.company.cli.maven_test.daos.primitives.typesBinary.selectByCriteria");
  }

  // insert

  public int insert(final com.company.cli.maven_test.daos.TypesBinaryVO vo) {
    String id = "com.company.cli.maven_test.daos.primitives.typesBinary.insert";
    return this.sqlSession.insert(id, vo);
  }

  // no update by PK generated, since the table does not have a PK.

  // no delete by PK generated, since the table does not have a PK.

  // update by example

  public int updateByExample(final com.company.cli.maven_test.daos.TypesBinaryVO example, final com.company.cli.maven_test.daos.TypesBinaryVO updateValues) {
    UpdateByExampleDao<com.company.cli.maven_test.daos.TypesBinaryVO> fvd = //
      new UpdateByExampleDao<com.company.cli.maven_test.daos.TypesBinaryVO>(example, updateValues);
    return this.sqlSession.update("com.company.cli.maven_test.daos.primitives.typesBinary.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final com.company.cli.maven_test.daos.TypesBinaryVO example) {
    return this.sqlSession.delete("com.company.cli.maven_test.daos.primitives.typesBinary.deleteByExample", example);
  }

  // DAO ordering

  public enum TypesBinaryOrderBy implements OrderBy {

    BIN1("types_binary", "bin1", true), //
    BIN1$DESC("types_binary", "bin1", false), //
    BOL1("types_binary", "bol1", true), //
    BOL1$DESC("types_binary", "bol1", false);

    private TypesBinaryOrderBy(final String tableName, final String columnName,
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

  public static TypesBinaryTable newTable() {
    return new TypesBinaryTable();
  }

  public static TypesBinaryTable newTable(final String alias) {
    return new TypesBinaryTable(alias);
  }

  public static class TypesBinaryTable extends Table {

    // Properties

    public ByteArrayColumn bin1;
    public BooleanColumn bol1;

    // Constructors

    TypesBinaryTable() {
      super(null, null, "types_binary", "Table", null);
      initialize();
    }

    TypesBinaryTable(final String alias) {
      super(null, null, "types_binary", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.bin1 = new ByteArrayColumn(this, "bin1", "bin1");
      this.bol1 = new BooleanColumn(this, "bol1", "bol1");
    }

  }

}
