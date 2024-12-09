package org.hotrod.dynamic;

public class DynamicParameterValue {

  private Object value;
  private int sqlType;

  private DynamicParameterValue(Object value, int sqlType) {
    this.value = value;
    this.sqlType = sqlType;
    if (this.value == null) {

    }

  }

  public static DynamicParameterValue of(Object value, int sqlType) {
    return new DynamicParameterValue(value, sqlType);
  }

  public Object getValue() {
    return value;
  }

  public int getSQLType() {
    return sqlType;
  }

}
