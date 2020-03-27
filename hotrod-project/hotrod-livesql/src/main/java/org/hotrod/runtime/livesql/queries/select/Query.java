package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;

public abstract class Query {

  protected SQLDialect sqlDialect;

  public Query(final SQLDialect sqlDialect) {
    if (sqlDialect == null) {
      throw new UnsupportedLiveSQLFeatureException("Undefined SQL dialect. Please specify the SQL dialect");
    }
    this.sqlDialect = sqlDialect;
  }

}
