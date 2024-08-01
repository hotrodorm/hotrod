package org.hotrod.runtime.typesolver;

public class UnresolvableDataTypeException extends Exception {

  private static final long serialVersionUID = 1L;

  private DriverColumnMetaData cm;

  public UnresolvableDataTypeException(final DriverColumnMetaData cm) {
    this.cm = cm;
  }

  public UnresolvableDataTypeException(final DriverColumnMetaData cm, final String message) {
    super(message);
    this.cm = cm;
  }

  public DriverColumnMetaData getColumnMetadata() {
    return this.cm;
  }

  @Deprecated // use medadata instead
  public String getTypeName() {
    return this.cm.getTypeName();
  }

  @Deprecated // use medadata instead
  public String getColumnName() {
    return this.cm.getName();
  }

  @Deprecated // use medadata instead
  public String getTableName() {
    return this.cm.getTable();
  }

}
