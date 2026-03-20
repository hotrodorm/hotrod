package org.hotrod.livesql.queries;

import java.sql.Connection;

import javax.sql.DataSource;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.queries.typesolver.RuntimeTypeSolver;
import org.springframework.jdbc.datasource.DataSourceUtils;

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

  public Connection getConnection() {
    return DataSourceUtils.getConnection(this.dataSource);
  }

  public void releaseConnection(Connection conn) {
    if (conn != null) {
      DataSourceUtils.releaseConnection(conn, this.dataSource);
    }
  }

  public RuntimeTypeSolver getTypeSolver() {
    return typeSolver;
  }

}
