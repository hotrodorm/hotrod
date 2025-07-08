package org.hotrod.livesql.queries.typesolver;

public enum TypeSource {

  GENERATION_DESIGNATED, // Designated at Generation Time, using <column java-type="" /> or <column converter="" />
  GENERATION_LAYER_RULE, // Resolved at Generation Time, by a Layer Rule, using <type-solver>/<gen>
  GENERATION_DIALECT_RULE, // Resolved at Generation Time, by a Static Dialect Default Rule

  RUNTIME_DESIGNATED, // Designated in LiveSQL using .type(class)
  RUNTIME_LAYER_RULE, // Resolved at runtime by a Runtime Layer Rule, using <type-solver>/<runtime>
  RUNTIME_DIALECT_RULE, // Resolved at runtime by a Runtime Dialect Default Rule

  RUNTIME_JDBC_DRIVER // Using layer default (option of last resort)
};
