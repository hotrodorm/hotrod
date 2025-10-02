package org.hotrod.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.util.ToString;

public class MShield {

//  public static List<Expression> renderAndReturnColumns(final MultiSet<?> multiSet) {
//    return multiSet.compileAndReturnColumns();
//  }

  public static void log(SelectObject<?> s, ToString t) {
    s.log(t);
  }

}
