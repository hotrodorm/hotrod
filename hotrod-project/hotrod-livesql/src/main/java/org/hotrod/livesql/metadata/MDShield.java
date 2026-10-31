package org.hotrod.livesql.metadata;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.UpdateTablePhase;

public class MDShield {

  public static void removeAlias(final TableOrView<?, ?> tov) {
    tov.removeAlias();
  }

  public static String renderUnescapedName(final TableOrView<?, ?> tov) {
    return tov.renderUnescapedName();
  }

  public static List<Expression> unwrap(SQLMetaExpression wc) {
    return wc.expand();
  }

  public static Name getName(TableOrView<?, ?> objectInstance) {
    return objectInstance.getName();
  }

  public static void renderTo(TableOrView<?, ?> objectInstance, final QueryWriter w) {
    objectInstance.renderTo(w);
  }

  public static Class<?> getLayoutClass(TableOrView<?, ?> tv) {
    return tv.layoutClass;
  }

  public static Class<?> getModelClass(TableOrView<?, ?> tv) {
    return tv.modelClass;
  }

  public static UpdateTablePhase getUpdatePhase(LiveSQLContext context, TableOrView<?, ?> tov) {
    return tov.getUpdatePhase(context);
  }

}
