package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;

public abstract class Query {

  protected LiveSQLDialect liveSQLDialect;

  public Query(final LiveSQLDialect sqlDialect) {
    if (sqlDialect == null) {
      throw new UnsupportedLiveSQLFeatureException("Undefined SQL dialect. Please specify the SQL dialect");
    }
    this.liveSQLDialect = sqlDialect;
  }

}
