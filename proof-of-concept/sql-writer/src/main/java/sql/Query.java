package sql;

import sql.exceptions.UnsupportedFeatureException;
import sql.sqldialects.SQLDialect;

public abstract class Query {

  protected SQLDialect sqlDialect;

  public Query(final SQLDialect sqlDialect) {
    if (sqlDialect == null) {
      throw new UnsupportedFeatureException("Undefined SQL dialect. Please specify the SQL dialect");
    }
    this.sqlDialect = sqlDialect;
  }

}
