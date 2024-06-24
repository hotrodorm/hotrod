package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;

public class MHelper {

//  public static LinkedHashMap<String, QueryColumn> getQueryColumns(final MultiSet<?> multiSet) {
//    return multiSet.getQueryColumns();
//  }

  public static List<Expression> assembleColumns(final MultiSet<?> multiSet) {
    return multiSet.assembleColumns();
  }

//  @Deprecated
//  public static void computeQueryColumns(final MultiSet<?> multiSet) {
//    multiSet.computeQueryColumns();
//  }

}
