package org.hotrod.livesql.ordering;

import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.QueryWriter;

public class OHelper {

  public static void renderTo(final OrderingTerm term, final QueryWriter w) {
    try {
      OrderingExpression oe = (OrderingExpression) term;
      oe.renderTo(w);
    } catch (ClassCastException e) {
      try {
        EquatableExpression ee = (EquatableExpression) term;
        Shield.renderTo(ee, w);
      } catch (ClassCastException e2) {
        throw new RuntimeException("Could not render OrderingTerm of class " + term.getClass().getName());
      }
    }
  }

}
