package org.hotrod.runtime.livesql.queries.typesolver;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;

public class TypeSolver {

  private static final Logger log = Logger.getLogger(TypeSolver.class.getName());

  private List<TypeRule> userRules;
  private LiveSQLDialect dialect;

  public TypeSolver(final List<TypeRule> userRules, final LiveSQLDialect dialect) {
    this.userRules = userRules;
    this.dialect = dialect;
  }

  public TypeHandler resolve(final ResultSetColumnMetadata cm) throws UnresolvableDataTypeException {

//    log.info("cm: " + cm);

    // 1. Try the user rules from the type-solver tag.

    if (this.userRules != null) {
      for (TypeRule r : this.userRules) {
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
