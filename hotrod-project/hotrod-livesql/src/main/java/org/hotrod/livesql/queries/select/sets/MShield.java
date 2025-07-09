package org.hotrod.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.util.ToString;

public class MShield {

  public static List<Expression> assembleColumnsOf(final MultiSet<?> multiSet, final Subquery sq) {
    return multiSet.assembleColumnsOf(sq);
  }

  public static void log(MultiSet<?> s, ToString t) {
    s.log(t);
  }

}
