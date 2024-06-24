package org.hotrod.runtime.livesql.metadata;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;

public class MDHelper {

  public static void removeAlias(final TableOrView tov) {
    tov.removeAlias();
  }

  public static String renderUnescapedName(final TableOrView tov) {
    return tov.renderUnescapedName();
  }

  public static List<Expression> unwrap(final WrappingColumn wc) {
    return wc.unwrap();
  }

}
