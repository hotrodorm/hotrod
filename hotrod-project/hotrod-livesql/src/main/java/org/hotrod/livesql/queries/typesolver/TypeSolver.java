package org.hotrod.livesql.queries.typesolver;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.queries.typesolver.TypeRule.CouldNotResolveResultSetDataTypeException;

public class TypeSolver {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TypeSolver.class.getName());

  private List<TypeRule> layerRules;
  private LiveSQLDialect dialect;

  public TypeSolver(final List<TypeRule> layerRules, final LiveSQLDialect dialect) {
    this.layerRules = layerRules;
    this.dialect = dialect;
  }

  public TypeHandler<?, ?> resolve(final ResultSetColumnMetadata cm) throws CouldNotResolveResultSetDataTypeException {

//    if (log.isLoggable(Level.FINE)) {
//      log.info("* " + cm);
//    }

    // 1. Try the layer rules from the type-solver tag.

    if (this.layerRules != null) {
      for (TypeRule r : this.layerRules) {
        TypeHandler<?, ?> th = r.resolve(cm);
        if (th != null) {
          return th;
        }
      }
    }

    // 2. Try the dialect rules (provided by default per the dialect)

    Class<?> c = this.dialect.resolveColumnType(cm);
    if (c != null) {
      return TypeHandler.forClass(c, TypeSource.RUNTIME_DIALECT_RULE);
    }

    // 3. Use the class proposed by the JDBC driver, if available

    String className = cm.getColumnClassName();
    if (className == null) {
      // The JDBC driver does not propose a default class;
      // needs to be set directly by user
      return null;
    }

    try {
      return TypeHandler.forClass(Class.forName(className), TypeSource.RUNTIME_JDBC_DRIVER_DEFAULT);
    } catch (ClassNotFoundException e) {
      throw new CouldNotResolveResultSetDataTypeException(cm,
          "The class '" + className + "' proposed by the JDBC driver to read the column cannot be found.");
    }

  }

}
