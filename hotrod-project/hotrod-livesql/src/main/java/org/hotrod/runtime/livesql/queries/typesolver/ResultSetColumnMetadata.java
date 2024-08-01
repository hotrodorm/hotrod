package org.hotrod.runtime.livesql.queries.typesolver;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.hotrod.runtime.typesolver.DriverColumnMetaData;

public class ResultSetColumnMetadata implements DriverColumnMetaData {

  private String catalog;
  private String schema;
  private String name;
  private String label;
  private String typeName;
  private Integer dataType;
  private String driverDefaultClassName;
  private Integer displaySize;
  private Integer precision;
  private Integer scale;

  // Constructor

  private ResultSetColumnMetadata(final ResultSetMetaData rm, final int ordinal) throws SQLException {
    this.catalog = nullIf(rm.getCatalogName(ordinal), "");
    this.schema = nullIf(rm.getSchemaName(ordinal), "");
    this.name = nullIf(rm.getColumnName(ordinal), "");
    this.label = nullIf(rm.getColumnLabel(ordinal), "");
    this.typeName = nullIf(rm.getColumnTypeName(ordinal), "");
    this.dataType = nullIf(rm.getColumnType(ordinal), 0);
    this.driverDefaultClassName = nullIf(rm.getColumnClassName(ordinal), "");
    this.displaySize = nullIf(rm.getColumnDisplaySize(ordinal), 0);
    this.precision = nullIf(rm.getPrecision(ordinal), 0);
    this.scale = nullIf(rm.getScale(ordinal), 0);
  }

  // Utils

  private String nullIf(final String a, final String b) {
    return a == null || a.equals(b) ? null : a;
  }

  private Integer nullIf(final int a, final int b) {
    return a == b ? null : a;
  }

  // Static instantiation

  public static ResultSetColumnMetadata of(final ResultSetMetaData rm, final int ordinal) throws SQLException {
    return new ResultSetColumnMetadata(rm, ordinal);
  }

  // Getters

  @Override
  public String getCatalog() {
    return this.catalog;
  }

  @Override
  public String getSchema() {
    return this.schema;
  }

  @Override
  public String getTable() {
    return null;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getLabel() {
    return this.label;
  }

  @Override
  public String getTypeName() {
    return this.typeName;
  }

  @Override
  public Integer getDataType() {
    return this.dataType;
  }

  @Override
  public String getDriverDefaultClassName() {
    return this.driverDefaultClassName;
  }

  @Override
  public Integer getDisplaySize() {
    return this.displaySize;
  }

  @Override
  public Integer getPrecision() {
    return this.precision;
  }

  @Override
  public Integer getScale() {
    return this.scale;
  }

}
