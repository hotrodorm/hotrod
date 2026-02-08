package org.hotrod.livesql.queries.typesolver;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.RuntimeType;
import org.hotrod.livesql.queries.typesolver.TypeRule.CouldNotResolveResultSetDataTypeException;

public class RuntimeTypeSolver {

  private static final Logger log = Logger.getLogger(RuntimeTypeSolver.class.getName());

  private List<TypeRule> runtimeTypeSolverRules;
  private LiveSQLDialect dialect;

  public RuntimeTypeSolver(final List<TypeRule> runtimeTypeSolverRules, final LiveSQLDialect dialect) {
    this.runtimeTypeSolverRules = runtimeTypeSolverRules;
    this.dialect = dialect;
  }

  public TypeHandler<?, ?> resolveRuntimeType(final ResultSetColumnMetadata cm)
      throws CouldNotResolveResultSetDataTypeException {

    // 1. Try the runtime type solver rules from the type-solver tag.

    if (this.runtimeTypeSolverRules != null) {
      for (TypeRule r : this.runtimeTypeSolverRules) {
        if (r.applies(cm)) {
          return r.getTypeHandler();
        }
      }
    }

    // 2. Try the dialect rules (provided by default per the dialect)

    RuntimeType rt = this.dialect.resolveRuntimeType(cm);
//    log.info("rt=" + rt);
    if (rt != null) {
      return TypeHandler.forClass(rt.getType(), TypeSource.RUNTIME_DIALECT_RULE, rt.getRuleNumber());
    }

    // 3. Use the class proposed by the JDBC driver, if available

    String className = cm.getColumnClassName();
//    log.info("JDBC Driver className=" + className);
    if (className == null) {
      // The JDBC driver does not propose a default class;
      // needs to be set directly by user
      return null;
    }

    try {
      return TypeHandler.forClass(Class.forName(className), TypeSource.RUNTIME_JDBC_DRIVER_DEFAULT, null);
    } catch (ClassNotFoundException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "The class '" + className + "' proposed by the JDBC driver to read the column cannot be found.");
    }

  }

}
