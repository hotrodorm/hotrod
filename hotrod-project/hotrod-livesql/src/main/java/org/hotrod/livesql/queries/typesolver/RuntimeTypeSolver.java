package org.hotrod.livesql.queries.typesolver;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.RuntimeType;
import org.hotrod.livesql.queries.typesolver.TypeRule.CouldNotResolveResultSetDataTypeException;

public class RuntimeTypeSolver {

  @SuppressWarnings("unused")
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

    log.info("Runtime TypeSolver 1");
    if (this.runtimeTypeSolverRules != null) {
      for (TypeRule r : this.runtimeTypeSolverRules) {
        log.info("Runtime TypeSolver RULE...");
        if (r.applies(cm)) {
          log.info("Runtime TypeSolver FOUND RULE: #" + r.getTypeHandler().getRuleNumber() + " class="
              + r.getTypeHandler().getClass().getName());
          return r.getTypeHandler();
        }
      }
    }

    // 2. Try the dialect rules (provided by default per the dialect)

    log.info("Runtime TypeSolver 2");
    RuntimeType rt = this.dialect.resolveRuntimeType(cm);
    if (rt != null) {
      log.info("DIALECT rt=" + rt.getType() + " : " + rt.getRuleNumber());
      return TypeHandler.forClass(rt.getType(), TypeSource.RUNTIME_DIALECT_RULE, rt.getRuleNumber());
    }

    // 3. Use the class proposed by the JDBC driver, if available

    log.info("Runtime TypeSolver 3");
    String className = cm.getColumnClassName();
    if (className == null) {
      // The JDBC driver does not propose a default class;
      // needs to be set directly by user
      return null;
    }

    log.info("Runtime TypeSolver 4");
    try {
      return TypeHandler.forClass(Class.forName(className), TypeSource.RUNTIME_JDBC_DRIVER_DEFAULT, null);
    } catch (ClassNotFoundException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "The class '" + className + "' proposed by the JDBC driver to read the column cannot be found.");
    }

  }

}
