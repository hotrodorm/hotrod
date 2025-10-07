package org.hotrod.livesql.queries;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.queries.typesolver.RuntimeTypeSolver;

public class LiveSQLContext {

  private LiveSQLDialect liveSQLDialect;
  private DataSource dataSource;
  private RuntimeTypeSolver typeSolver;
  private Logger logger;

  public LiveSQLContext(final LiveSQLDialect liveSQLDialect, final DataSource dataSource,
      final RuntimeTypeSolver typeSolver, final Logger logger) {
    this.liveSQLDialect = liveSQLDialect;
    this.dataSource = dataSource;
    this.typeSolver = typeSolver;
    this.logger = logger;
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
