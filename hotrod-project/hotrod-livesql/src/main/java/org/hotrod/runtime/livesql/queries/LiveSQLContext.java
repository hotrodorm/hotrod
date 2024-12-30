package org.hotrod.runtime.livesql.queries;

import javax.sql.DataSource;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;

public class LiveSQLContext {

  private LiveSQLDialect liveSQLDialect;
  private DataSource dataSource;
  private TypeSolver typeSolver;

  public LiveSQLContext(final LiveSQLDialect liveSQLDialect, final DataSource dataSource, final TypeSolver typeSolver) {
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

  public TypeSolver getTypeSolver() {
    return typeSolver;
  }

}
