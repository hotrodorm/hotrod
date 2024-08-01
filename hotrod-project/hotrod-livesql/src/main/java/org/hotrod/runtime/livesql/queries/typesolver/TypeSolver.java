package org.hotrod.runtime.livesql.queries.typesolver;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;

public class TypeSolver {

  private List<TypeRule> userRules = new ArrayList<>();
  private List<TypeRule> dialectRules = new ArrayList<>();

  public void addUserRule(final TypeRule r) {
    this.userRules.add(r);
  }

  public void addDialectRule(final TypeRule r) {
    this.dialectRules.add(r);
  }

  public TypeHandler resolve(final ResultSetColumnMetadata cm) throws UnresolvableDataTypeException {

    // 1. Try the user rules from the type-solver tag.

    for (TypeRule r : this.userRules) {
      TypeHandler th = r.resolve(cm);
      if (th != null) {
        return th;
      }
    }

    // 2. Try the dialect rules (provided by default per the dialect)

    for (TypeRule r : this.userRules) {
      TypeHandler th = r.resolve(cm);
      if (th != null) {
        return th;
      }
    }

    // 3. There's no rule for this type. Needs to be set directly by user

    return null;
  }

}
