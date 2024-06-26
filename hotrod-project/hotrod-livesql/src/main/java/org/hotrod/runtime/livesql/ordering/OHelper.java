package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class OHelper {

  public static void renderTo(final OrderingTerm term, final QueryWriter w) {
    try {
      OrderingExpression oe = (OrderingExpression) term;
      oe.renderTo(w);
    } catch (ClassCastException e) {
      throw new RuntimeException("Could not render OrderingTerm of class " + term.getClass().getName());
    }
  }

}
