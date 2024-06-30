package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.queries.subqueries.EmergingColumn;

public class MHelper {

  public static List<EmergingColumn> assembleColumnsOf(final MultiSet<?> multiSet, final TableExpression te) {
    return multiSet.assembleColumnsOf(te);
  }

}
