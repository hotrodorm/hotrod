package org.hotrod.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.subqueries.Subquery;

public class MShield {

  public static List<Expression> assembleColumnsOf(final MultiSet<?> multiSet, final Subquery sq) {
    return multiSet.assembleColumnsOf(sq);
  }

}
