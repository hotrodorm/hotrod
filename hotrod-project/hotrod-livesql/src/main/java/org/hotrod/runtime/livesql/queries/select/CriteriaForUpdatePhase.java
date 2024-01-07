package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public class CriteriaForUpdatePhase<T> extends CriteriaPhase<T> {

  public CriteriaForUpdatePhase(final LiveSQLContext context, final AbstractSelectObject<T> select,
      final String mapperStatement) {
    super(context, select, mapperStatement);
  }

  // next phases

}
