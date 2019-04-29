package org.hotrod.runtime.sql;

import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.sql.sqldialects.SQLDialect;

public abstract class Query {

  protected SQLDialect sqlDialect;

  public Query(final SQLDialect sqlDialect) {
    if (sqlDialect == null) {
      throw new UnsupportedFeatureException("Undefined SQL dialect. Please specify the SQL dialect");
    }
    this.sqlDialect = sqlDialect;
  }

}
