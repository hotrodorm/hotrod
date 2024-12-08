package org.hotrod.dynamic;

public class DynamicParameter {

  private Object value;
  private int sqlType;

  private DynamicParameter(Object value, int sqlType) {
    this.value = value;
    this.sqlType = sqlType;
    if (this.value == null) {

    }

  }

  public static DynamicParameter of(Object value, int sqlType) {
    return new DynamicParameter(value, sqlType);
  }

  public Object getValue() {
    return value;
  }

  public int getSQLType() {
    return sqlType;
  }

}
