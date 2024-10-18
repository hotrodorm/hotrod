package org.hotrod.runtime.livesql.queries.typesolver;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.typesolver.TypeRule.CouldNotResolveResultSetDataTypeException;

public class TypeSolver {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TypeSolver.class.getName());

  private List<TypeRule> layerRules;
  private LiveSQLDialect dialect;

  public TypeSolver(final List<TypeRule> layerRules, final LiveSQLDialect dialect) {
    this.layerRules = layerRules;
    this.dialect = dialect;
  }

  public TypeHandler resolve(final ResultSetColumnMetadata cm) throws CouldNotResolveResultSetDataTypeException {

    log.info("--- cm: " + cm);

    // 1. Try the layer rules from the type-solver tag.

    if (this.layerRules != null) {
      for (TypeRule r : this.layerRules) {
        TypeHandler th = r.resolve(cm);
        if (th != null) {
          return th;
        }
      }
    }

    // 2. Try the dialect rules (provided by default per the dialect)

    Class<?> c = this.dialect.resolveColumnType(cm);
//    log.info("col: " + cm.getName() + " - c=" + c);

    if (c != null) {
      return TypeHandler.of(c);
    }

    // 3. There's no rule for this type. Needs to be set directly by user

    return null;

  }

}
