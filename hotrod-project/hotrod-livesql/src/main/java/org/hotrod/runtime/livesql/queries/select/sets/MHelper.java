package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.TableExpression;

public class MHelper {

  @Deprecated // deprecated?
  public static List<Expression> assembleColumnsOf(final MultiSet<?> multiSet, final TableExpression te) {
    return multiSet.assembleColumnsOf(te);
  }

}
