package org.hotrod.livesql.metadata;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;

public class MDShield {

  public static void removeAlias(final TableOrView<?> tov) {
    tov.removeAlias();
  }

  public static String renderUnescapedName(final TableOrView<?> tov) {
    return tov.renderUnescapedName();
  }

  public static List<Expression> unwrap(WrappingColumn wc) {
    return wc.unwrap();
  }

  public static Name getName(TableOrView<?> objectInstance) {
    return objectInstance.getName();
  }

  public static void renderTo(TableOrView<?> objectInstance, final QueryWriter w) {
    objectInstance.renderTo(w);
  }

  public static Class<?> getModelClass(TableOrView<?> tv) {
    return tv.modelClass;
  }

}
