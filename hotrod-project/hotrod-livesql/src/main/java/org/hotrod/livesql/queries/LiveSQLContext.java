package org.hotrod.livesql.queries;

import javax.sql.DataSource;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.queries.typesolver.RuntimeTypeSolver;

public class LiveSQLContext {

  private LiveSQLDialect liveSQLDialect;
  private DataSource dataSource;
  private RuntimeTypeSolver typeSolver;

  public LiveSQLContext(final LiveSQLDialect liveSQLDialect, final DataSource dataSource,
      final RuntimeTypeSolver typeSolver) {
    this.liveSQLDialect = liveSQLDialect;
    this.dataSource = dataSource;
    this.typeSolver = typeSolver;
  }

  public LiveSQLDialect getLiveSQLDialect() {
    return liveSQLDialect;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public RuntimeTypeSolver getTypeSolver() {
    return typeSolver;
  }

}
