package org.hotrod.livesql.queries.typesolver;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetColumnMetadata {

  private String catalogName;
  private String columnClassName;
  private int columnDisplaySize;
  private String columnLabel;
  private String columnName;
  private int columnType;
  private String columnTypeName;
  private int precision;
  private int scale;
  private String schemaName;
  private String tableName;
  private boolean autoIncrement;
  private boolean caseSensitive;
  private boolean currency;
  private boolean definitelyWritable;
  private int nullable;
  private boolean readOnly;
  private boolean searchable;
  private boolean signed;
  private boolean writable;

  // Constructor

  private ResultSetColumnMetadata(final ResultSetMetaData rm, final int column) throws SQLException {
    this.catalogName = rm.getCatalogName(column);
    this.columnClassName = rm.getColumnClassName(column);
    this.columnDisplaySize = rm.getColumnDisplaySize(column);
    this.columnLabel = rm.getColumnLabel(column);
    this.columnName = rm.getColumnName(column);
    this.columnType = rm.getColumnType(column);
    this.columnTypeName = rm.getColumnTypeName(column);
    this.precision = rm.getPrecision(column);
    this.scale = rm.getScale(column);
    this.schemaName = rm.getSchemaName(column);
    this.tableName = rm.getTableName(column);
    this.autoIncrement = rm.isAutoIncrement(column);
    this.caseSensitive = rm.isCaseSensitive(column);
    this.currency = rm.isCurrency(column);
    this.definitelyWritable = rm.isDefinitelyWritable(column);
    this.nullable = rm.isNullable(column);
    this.readOnly = rm.isReadOnly(column);
    this.searchable = rm.isSearchable(column);
    this.signed = rm.isSigned(column);
    this.writable = rm.isWritable(column);
  }

  // Static instantiation

  public static ResultSetColumnMetadata of(final ResultSetMetaData rm, final int ordinal) throws SQLException {
    return new ResultSetColumnMetadata(rm, ordinal);
  }

  // Getters

  public String getCatalogName() {
    return this.catalogName;
  }

  public String getColumnClassName() {
    return this.columnClassName;
  }

  public int getColumnDisplaySize() {
    return this.columnDisplaySize;
  }

  public String getColumnLabel() {
    return this.columnLabel;
  }

  public String getColumnName() {
    return this.columnName;
  }

  public int getColumnType() {
    return this.columnType;
  }

  public String getColumnTypeName() {
    return this.columnTypeName;
  }

  public int getPrecision() {
    return this.precision;
  }

  public int getScale() {
    return this.scale;
  }

  public String getSchemaName() {
    return this.schemaName;
  }

  public String getTableName() {
    return tableName;
  }

  public boolean isAutoIncrement() {
    return autoIncrement;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public boolean isCurrency() {
    return currency;
  }

  public boolean isDefinitelyWritable() {
    return definitelyWritable;
  }

  public int getNullable() {
    return nullable;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public boolean isSearchable() {
    return searchable;
  }

  public boolean isSigned() {
    return signed;
  }

  public boolean isWritable() {
    return writable;
  }

  @Override
  public String toString() {
    return "catalogName=" + catalogName + ",\nschemaName=" + schemaName + ",\ncolumnName="
        + columnName + ",\ncolumnLabel=" + columnLabel + ",\ncolumnTypeName=" + columnTypeName + ",\ncolumnType="
        + columnType + ",\ncolumnClassName=" + columnClassName + ",\ncolumnDisplaySize=" + columnDisplaySize
        + ",\nprecision=" + precision + ",\nscale=" + scale + ",\ntableName=" + tableName + ",\nautoIncrement="
        + autoIncrement + ",\ncaseSensitive=" + caseSensitive + ",\ncurrency=" + currency + ",\ndefinitelyWritable="
        + definitelyWritable + ",\nnullable=" + nullable + ",\nreadOnly=" + readOnly + ",\nsearchable=" + searchable
        + ",\nsigned=" + signed + ",\nwritable=" + writable;
  }

}
