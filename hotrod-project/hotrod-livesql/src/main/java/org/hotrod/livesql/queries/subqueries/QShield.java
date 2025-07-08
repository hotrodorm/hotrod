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

  public static void setColumn(SubqueryStringRefColumn sc, Expression column) {
    sc.setColumn(column);
  }

  public static void setColumn(SubqueryDateTimeRefColumn sc, Expression column) {
    sc.setColumn(column);
  }

  public static void setColumn(SubqueryBooleanRefColumn sc, Expression column) {
    sc.setColumn(column);
  }

  public static void setColumn(SubqueryByteArrayRefColumn sc, Expression column) {
    sc.setColumn(column);
  }

  public static void setColumn(SubqueryObjectRefColumn sc, Expression column) {
    sc.setColumn(column);
  }

}
