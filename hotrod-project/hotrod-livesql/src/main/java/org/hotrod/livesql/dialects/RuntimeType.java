package org.hotrod.livesql.dialects;

public class RuntimeType {

  private static final String DIALECT = "d";
  private static final String TYPESOLVER = "t";

  private Class<?> type;
  private String namespace;
  private Integer ruleNumber;

  private RuntimeType(Class<?> type, Integer ruleNumber, String namespace) {
    this.type = type;
    this.namespace = namespace;
    this.ruleNumber = ruleNumber;
  }

  public static RuntimeType ofDialect(Class<?> type, Integer ruleNumber) {
    return new RuntimeType(type, ruleNumber, DIALECT);
  }

  public static RuntimeType ofTypeSolver(Class<?> type, Integer ruleNumber) {
    return new RuntimeType(type, ruleNumber, TYPESOLVER);
  }

  public final Class<?> getType() {
    return type;
  }

  public final String getRuleNumber() {
    return this.ruleNumber == null ? null : this.namespace + ruleNumber;
  }

}
