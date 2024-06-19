package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.LinkedHashMap;

import org.hotrod.runtime.livesql.queries.QueryColumn;

public class MHelper {

  public static LinkedHashMap<String, QueryColumn> getQueryColumns(final MultiSet<?> multiSet) {
    return multiSet.getQueryColumns();
  }

  public static void computeQueryColumns(final MultiSet<?> multiSet) {
    multiSet.computeQueryColumns();
  }

}
