package org.hotrod.livesql.queries.subqueries;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.metadata.Name;

public class QShield {

  public static Name getName(Subquery s) {
    return s.getName();
  }

  public static void setColumn(SubqueryNumberRefColumn sc, Expression column) {
    sc.setColumn(column);
  }
  
}
