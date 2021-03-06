// Autogenerated by HotRod -- Do not edit.

package com.company.daos.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import com.company.daos.TypesDateTimeVO;

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

@Component("typesDateTimeDAO")
public class TypesDateTimeDAO implements Serializable, ApplicationContextAware {

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

  // select by primary key

  public com.company.daos.TypesDateTimeVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    com.company.daos.TypesDateTimeVO vo = new com.company.daos.TypesDateTimeVO();
    vo.setId(id);
    return this.sqlSession.selectOne("com.company.daos.primitives.typesDateTime.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<com.company.daos.TypesDateTimeVO> selectByExample(final com.company.daos.TypesDateTimeVO example, final TypesDateTimeOrderBy... orderBies)
      {
    DaoWithOrder<com.company.daos.TypesDateTimeVO, TypesDateTimeOrderBy> dwo = //
        new DaoWithOrder<com.company.daos.TypesDateTimeVO, TypesDateTimeOrderBy>(example, orderBies);
    return this.sqlSession.selectList("com.company.daos.primitives.typesDateTime.selectByExample", dwo);
  }

  // select by criteria

  public CriteriaWherePhase<com.company.daos.TypesDateTimeVO> selectByCriteria(final TypesDateTimeDAO.TypesDateTimeTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<com.company.daos.TypesDateTimeVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "com.company.daos.primitives.typesDateTime.selectByCriteria");
  }

  // insert

  public int insert(final com.company.daos.TypesDateTimeVO vo) {
    String id = "com.company.daos.primitives.typesDateTime.insert";
    return this.sqlSession.insert(id, vo);
  }

  // update by PK

  public int update(final com.company.daos.TypesDateTimeVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("com.company.daos.primitives.typesDateTime.updateByPK", vo);
  }

  // delete by PK

  public int delete(final com.company.daos.TypesDateTimeVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("com.company.daos.primitives.typesDateTime.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final com.company.daos.TypesDateTimeVO example, final com.company.daos.TypesDateTimeVO updateValues) {
    UpdateByExampleDao<com.company.daos.TypesDateTimeVO> fvd = //
      new UpdateByExampleDao<com.company.daos.TypesDateTimeVO>(example, updateValues);
    return this.sqlSession.update("com.company.daos.primitives.typesDateTime.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final com.company.daos.TypesDateTimeVO example) {
    return this.sqlSession.delete("com.company.daos.primitives.typesDateTime.deleteByExample", example);
  }

  // DAO ordering

  public enum TypesDateTimeOrderBy implements OrderBy {

    ID("types_date_time", "id", true), //
    ID$DESC("types_date_time", "id", false), //
    DAT1("types_date_time", "dat1", true), //
    DAT1$DESC("types_date_time", "dat1", false), //
    TS1("types_date_time", "ts1", true), //
    TS1$DESC("types_date_time", "ts1", false), //
    TS2("types_date_time", "ts2", true), //
    TS2$DESC("types_date_time", "ts2", false), //
    TS3("types_date_time", "ts3", true), //
    TS3$DESC("types_date_time", "ts3", false), //
    TS4("types_date_time", "ts4", true), //
    TS4$DESC("types_date_time", "ts4", false), //
    TS5("types_date_time", "ts5", true), //
    TS5$DESC("types_date_time", "ts5", false), //
    TIM1("types_date_time", "tim1", true), //
    TIM1$DESC("types_date_time", "tim1", false), //
    TIM2("types_date_time", "tim2", true), //
    TIM2$DESC("types_date_time", "tim2", false), //
    TIM3("types_date_time", "tim3", true), //
    TIM3$DESC("types_date_time", "tim3", false), //
    TIM4("types_date_time", "tim4", true), //
    TIM4$DESC("types_date_time", "tim4", false), //
    TIM5("types_date_time", "tim5", true), //
    TIM5$DESC("types_date_time", "tim5", false);

    private TypesDateTimeOrderBy(final String tableName, final String columnName,
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

  public static TypesDateTimeTable newTable() {
    return new TypesDateTimeTable();
  }

  public static TypesDateTimeTable newTable(final String alias) {
    return new TypesDateTimeTable(alias);
  }

  public static class TypesDateTimeTable extends Table {

    // Properties

    public NumberColumn id;
    public DateTimeColumn dat1;
    public DateTimeColumn ts1;
    public DateTimeColumn ts2;
    public DateTimeColumn ts3;
    public DateTimeColumn ts4;
    public DateTimeColumn ts5;
    public DateTimeColumn tim1;
    public DateTimeColumn tim2;
    public DateTimeColumn tim3;
    public DateTimeColumn tim4;
    public DateTimeColumn tim5;

    // Constructors

    TypesDateTimeTable() {
      super(null, null, "types_date_time", "Table", null);
      initialize();
    }

    TypesDateTimeTable(final String alias) {
      super(null, null, "types_date_time", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.dat1 = new DateTimeColumn(this, "dat1", "dat1");
      this.ts1 = new DateTimeColumn(this, "ts1", "ts1");
      this.ts2 = new DateTimeColumn(this, "ts2", "ts2");
      this.ts3 = new DateTimeColumn(this, "ts3", "ts3");
      this.ts4 = new DateTimeColumn(this, "ts4", "ts4");
      this.ts5 = new DateTimeColumn(this, "ts5", "ts5");
      this.tim1 = new DateTimeColumn(this, "tim1", "tim1");
      this.tim2 = new DateTimeColumn(this, "tim2", "tim2");
      this.tim3 = new DateTimeColumn(this, "tim3", "tim3");
      this.tim4 = new DateTimeColumn(this, "tim4", "tim4");
      this.tim5 = new DateTimeColumn(this, "tim5", "tim5");
    }

  }

}
