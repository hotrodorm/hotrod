package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.livesql.queries.subqueries.EmergingColumn;

public class MHelper {

  public static List<EmergingColumn> assembleColumns(final MultiSet<?> multiSet) {
    return multiSet.assembleColumns();
  }

}
