package org.hotrod.runtime.livesql;

import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.exceptions.UnsupportedFeatureException;

public abstract class Query {

  protected SQLDialect sqlDialect;

  public Query(final SQLDialect sqlDialect) {
    if (sqlDialect == null) {
      throw new UnsupportedFeatureException("Undefined SQL dialect. Please specify the SQL dialect");
    }
    this.sqlDialect = sqlDialect;
  }

}
