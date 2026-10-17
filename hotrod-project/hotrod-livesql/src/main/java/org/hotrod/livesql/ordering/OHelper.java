package org.hotrod.livesql.ordering;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.queries.QueryWriter;

public class OHelper {

  public static void renderTo(final OrderingTerm term, final QueryWriter w) {
    if (term instanceof OrderingExpression) {
      OrderingExpression oe = (OrderingExpression) term;
      oe.renderTo(w);
    } else if (term instanceof EquatableExpression) {
      EquatableExpression ee = (EquatableExpression) term;
      Shield.renderTo(ee, w);
    } else {
      throw new LiveSQLException("Could not render OrderingTerm of class " + term.getClass().getName());
    }
  }

}
