package org.hotrod.livesql.dialects;

import org.hotrod.typesolver.TypeSolverConst;

public class RuntimeType {

  private Class<?> type;
  private String namespace;
  private Integer ruleNumber;

  private RuntimeType(Class<?> type, Integer ruleNumber, String namespace) {
    this.type = type;
    this.namespace = namespace;
    this.ruleNumber = ruleNumber;
  }

  public static RuntimeType ofDialect(Class<?> type, Integer ruleNumber) {
    return new RuntimeType(type, ruleNumber, TypeSolverConst.RUNTIME_DIALECT_NAMESPACE);
  }

  public static RuntimeType ofTypeSolver(Class<?> type, Integer ruleNumber) {
    return new RuntimeType(type, ruleNumber, TypeSolverConst.RUNTIME_TYPESOLVER_NAMESPACE);
  }

  public final Class<?> getType() {
    return type;
  }

  public final String getRuleNumber() {
    return this.ruleNumber == null ? null : this.namespace + ruleNumber;
  }

  @Override
  public String toString() {
    return "RuntimeType [type=" + type + ", namespace=" + namespace + ", ruleNumber=" + ruleNumber + "]";
  }

}
