package org.hotrod.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.select.TableExpression;

public class MHelper {

  public static List<Expression> assembleColumnsOf(final MultiSet<?> multiSet, final TableExpression te) {
    return multiSet.assembleColumnsOf(te);
  }

}
