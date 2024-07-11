package org.hotrod.runtime.typesolver;

import java.util.ArrayList;
import java.util.List;

public class TypeSolver {

  private List<TypeRule> userRules = new ArrayList<>();
  private List<TypeRule> dialectRules = new ArrayList<>();

  public void addUserRule(final TypeRule r) {
    this.userRules.add(r);
  }

  public void addDialectRule(final TypeRule r) {
    this.dialectRules.add(r);
  }

  public TypeHandler resolve() {
    return null;
  }

}
