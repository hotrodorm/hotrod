package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;

public abstract class AssembledQuery {

  protected LiveSQLContext context;

  public AssembledQuery(final LiveSQLContext context) {
    if (context == null) {
      throw new UnsupportedLiveSQLFeatureException("Undefined LiveSQL context. Please specify the SQL dialect");
    }
    if (context.getLiveSQLDialect() == null) {
      throw new UnsupportedLiveSQLFeatureException("Undefined SQL dialect. Please specify the SQL dialect");
    }
    this.context = context;
  }

}
