// Autogenerated by HotRod -- Do not edit.

package com.company.cli.maven_test.daos.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import org.hotrod.runtime.interfaces.DaoForUpdate;
import org.hotrod.runtime.exceptions.StaleDataException;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import com.company.cli.maven_test.daos.VehicleVO;
import com.company.cli.maven_test.daos.primitives.VehicleType;

import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.hotrod.runtime.converter.TypeConverter;

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

@Component("vehicleDAO")
public class VehicleDAO implements Serializable, ApplicationContextAware {

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

  public com.company.cli.maven_test.daos.VehicleVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    com.company.cli.maven_test.daos.VehicleVO vo = new com.company.cli.maven_test.daos.VehicleVO();
    vo.setId(id);
    return this.sqlSession.selectOne("com.company.cli.maven_test.daos.primitives.vehicle.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<com.company.cli.maven_test.daos.VehicleVO> selectByExample(final com.company.cli.maven_test.daos.VehicleVO example, final VehicleOrderBy... orderBies)
      {
    DaoWithOrder<com.company.cli.maven_test.daos.VehicleVO, VehicleOrderBy> dwo = //
        new DaoWithOrder<com.company.cli.maven_test.daos.VehicleVO, VehicleOrderBy>(example, orderBies);
    return this.sqlSession.selectList("com.company.cli.maven_test.daos.primitives.vehicle.selectByExample", dwo);
  }

  // select by criteria

  public CriteriaWherePhase<com.company.cli.maven_test.daos.VehicleVO> selectByCriteria(final VehicleDAO.VehicleTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<com.company.cli.maven_test.daos.VehicleVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "com.company.cli.maven_test.daos.primitives.vehicle.selectByCriteria");
  }

  // insert

  public int insert(final com.company.cli.maven_test.daos.VehicleVO vo) {
    return insert(vo, false);
  }

  public int insert(final com.company.cli.maven_test.daos.VehicleVO vo, final boolean retrieveDefaults) {
    vo.versionNumber = (short) 4;
    String id = retrieveDefaults ? "com.company.cli.maven_test.daos.primitives.vehicle.insertRetrievingDefaults" : "com.company.cli.maven_test.daos.primitives.vehicle.insert";
    int rows = this.sqlSession.insert(id, vo);
    return rows;
  }

  // update by PK

  public int update(final com.company.cli.maven_test.daos.VehicleVO vo) {
    long currentVersion = vo.versionNumber;
    DaoForUpdate<com.company.cli.maven_test.daos.VehicleVO> u = new DaoForUpdate<com.company.cli.maven_test.daos.VehicleVO>(vo, currentVersion, (short) -7, (short) 7);
    int rows = this.sqlSession.update("com.company.cli.maven_test.daos.primitives.vehicle.updateByPK", u);
    if (rows != 1) {
      throw new StaleDataException("Could not update row on table vehicle with version " + currentVersion
          + " since it had already been updated by another process.");
    }
    vo.versionNumber = (short) u.getNextVersionValue();
    return rows;
  }

  // delete by PK

  public int delete(final com.company.cli.maven_test.daos.VehicleVO vo) {
    int rows = this.sqlSession.delete("com.company.cli.maven_test.daos.primitives.vehicle.deleteByPK", vo);
    if (rows != 1) {
      throw new StaleDataException("Could not delete row on table vehicle with version " + vo.versionNumber
          + " since it had already been updated or deleted by another process.");
    }
    return rows;
  }

  // update by example

  public int updateByExample(final com.company.cli.maven_test.daos.VehicleVO example, final com.company.cli.maven_test.daos.VehicleVO updateValues) {
    UpdateByExampleDao<com.company.cli.maven_test.daos.VehicleVO> fvd = //
      new UpdateByExampleDao<com.company.cli.maven_test.daos.VehicleVO>(example, updateValues);
    return this.sqlSession.update("com.company.cli.maven_test.daos.primitives.vehicle.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final com.company.cli.maven_test.daos.VehicleVO example) {
    return this.sqlSession.delete("com.company.cli.maven_test.daos.primitives.vehicle.deleteByExample", example);
  }

  // TypeHandler for enum-FK column vtype.

  public static class VtypeTypeHandler implements TypeHandler<com.company.cli.maven_test.daos.primitives.VehicleType> {

    @Override
    public com.company.cli.maven_test.daos.primitives.VehicleType getResult(final ResultSet rs, final String columnName) throws SQLException {
      java.lang.Integer value = rs.getInt(columnName);
      if (rs.wasNull()) {
        value = null;
      }
      return com.company.cli.maven_test.daos.primitives.VehicleType.decode(value);
    }

    @Override
    public com.company.cli.maven_test.daos.primitives.VehicleType getResult(final ResultSet rs, final int columnIndex) throws SQLException {
      java.lang.Integer value = rs.getInt(columnIndex);
      if (rs.wasNull()) {
        value = null;
      }
      return com.company.cli.maven_test.daos.primitives.VehicleType.decode(value);
    }

    @Override
    public com.company.cli.maven_test.daos.primitives.VehicleType getResult(final CallableStatement cs, final int columnIndex) throws SQLException {
      java.lang.Integer value = cs.getInt(columnIndex);
      if (cs.wasNull()) {
        value = null;
      }
      return com.company.cli.maven_test.daos.primitives.VehicleType.decode(value);
    }

    @Override
    public void setParameter(final PreparedStatement ps, final int columnIndex, final com.company.cli.maven_test.daos.primitives.VehicleType v, final JdbcType jdbcType)
        throws SQLException {
      java.lang.Integer importedValue = com.company.cli.maven_test.daos.primitives.VehicleType.encode(v);
      java.lang.Integer localValue = importedValue;
      if (localValue == null) {
        ps.setNull(columnIndex, jdbcType.TYPE_CODE);
      } else {
        ps.setInt(columnIndex, localValue);
      }
    }

  }

  // DAO ordering

  public enum VehicleOrderBy implements OrderBy {

    ID("vehicle", "id", true), //
    ID$DESC("vehicle", "id", false), //
    NAME("vehicle", "name", true), //
    NAME$DESC("vehicle", "name", false), //
    NAME$CASEINSENSITIVE("vehicle", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("vehicle", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("vehicle", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("vehicle", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("vehicle", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("vehicle", "lower(name), name", true), //
    MILEAGE("vehicle", "mileage", true), //
    MILEAGE$DESC("vehicle", "mileage", false), //
    VERSION_NUMBER("vehicle", "version_number", true), //
    VERSION_NUMBER$DESC("vehicle", "version_number", false), //
    VTYPE("vehicle", "vtype", true), //
    VTYPE$DESC("vehicle", "vtype", false);

    private VehicleOrderBy(final String tableName, final String columnName,
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

  public static VehicleTable newTable() {
    return new VehicleTable();
  }

  public static VehicleTable newTable(final String alias) {
    return new VehicleTable(alias);
  }

  public static class VehicleTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn name;
    public NumberColumn mileage;
    public NumberColumn versionNumber;
    public ObjectColumn vtype;

    // Constructors

    VehicleTable() {
      super(null, null, "vehicle", "Table", null);
      initialize();
    }

    VehicleTable(final String alias) {
      super(null, null, "vehicle", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.name = new StringColumn(this, "name", "name");
      this.mileage = new NumberColumn(this, "mileage", "mileage");
      this.versionNumber = new NumberColumn(this, "version_number", "versionNumber");
      this.vtype = new ObjectColumn(this, "vtype", "vtype");
    }

  }

}