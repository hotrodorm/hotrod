package org.hotrod.runtime.util;

public class SQLField {
  private SQLTable table;
  private String fieldName;

  public SQLField(SQLTable table, String fieldName) {
    this.table = table;
    this.fieldName = fieldName;
  }

  public SQLField(String fieldName) {
    this(null, fieldName);
  }

  public String render() {
    return (table != null && table.getAlias() != null ? fieldName.replaceFirst(table.getName(), table.getAlias())
        : fieldName);
  }

  public SQLTable getTable() {
    return table;
  }

  public String getFieldName() {
    return fieldName;
  }

}
