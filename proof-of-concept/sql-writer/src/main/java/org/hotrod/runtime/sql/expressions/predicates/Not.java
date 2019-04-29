package org.hotrod.runtime.sql.expressions.predicates;

import org.hotrod.runtime.sql.QueryWriter;

public class Not extends Predicate {

  private static final int PRECEDENCE = 2;

  private Predicate a;

  public Not(final Predicate a) {
    super(PRECEDENCE);
    this.a = a;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("not (");
    super.renderInner(this.a, w);
    w.write(")");
  }

}
