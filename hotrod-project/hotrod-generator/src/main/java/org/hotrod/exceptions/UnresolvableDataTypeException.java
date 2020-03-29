package org.hotrod.exceptions;

import org.hotrod.metadata.ColumnMetadata;

public class UnresolvableDataTypeException extends Exception {

  private static final long serialVersionUID = 1L;

  private ColumnMetadata cm;

  public UnresolvableDataTypeException(final ColumnMetadata cm) {
    this.cm = cm;
  }

  public UnresolvableDataTypeException(final ColumnMetadata cm, final String message) {
    super(message);
    this.cm = cm;
  }

  // public JdbcColumn getColumn() {
  // return column;
  // }

  public ColumnMetadata getColumnMetadata() {
    return cm;
  }

  public String getTypeName() {
    return this.cm.getTypeName();
  }

  public String getColumnName() {
    return this.cm.getColumnName();
  }

  public String getTableName() {
    return this.cm.getTableName();
  }

}
