package org.hotrod.runtime.livesql.queries;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;

public class LiveSQLContext {

  private LiveSQLDialect liveSQLDialect;
  private DataSource dataSource;
  private TypeSolver typeSolver;
  private Logger logger;

  public LiveSQLContext(final LiveSQLDialect liveSQLDialect, final DataSource dataSource, final TypeSolver typeSolver,
      final Logger logger) {
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

  public TypeSolver getTypeSolver() {
    return typeSolver;
  }

  public Logger getLogger() {
    return logger;
  }

}
