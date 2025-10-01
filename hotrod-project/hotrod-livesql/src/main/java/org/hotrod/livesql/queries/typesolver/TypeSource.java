package org.hotrod.livesql.queries.typesolver;

public enum TypeSource {

  STATIC_DESIGNATED, // Designated at Generation Time, using <column java-type="" /> or <column
                     // converter="" />
  STATIC_TYPESOLVER_RULE, // Resolved at Generation Time, by a Layer Rule, using <type-solver>/<static>
  STATIC_DIALECT_RULE, // Resolved at Generation Time, by a Static Dialect Default Rule

  RUNTIME_DESIGNATED, // Designated in LiveSQL using .type(class)
  RUNTIME_TYPESOLVER_RULE, // Resolved at runtime by a Runtime Layer Rule, using <type-solver>/<runtime>
  RUNTIME_DIALECT_RULE, // Resolved at runtime by a Runtime Dialect Default Rule

  RUNTIME_JDBC_DRIVER_DEFAULT // Using JDBC driver default (option of last resort)
};
