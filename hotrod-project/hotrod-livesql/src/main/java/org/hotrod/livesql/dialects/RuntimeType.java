package org.hotrod.livesql.dialects;

public class RuntimeType {

  private Class<?> type;
  private Integer ruleNumber;

  private RuntimeType(Class<?> type, Integer ruleNumber) {
    this.type = type;
    this.ruleNumber = ruleNumber;
  }

  public static RuntimeType of(Class<?> type, Integer ruleNumber) {
    return new RuntimeType(type, ruleNumber);
  }

  public final Class<?> getType() {
    return type;
  }

  public final String getRuleNumber() {
    return this.ruleNumber == null ? null : "R" + ruleNumber;
  }

}
