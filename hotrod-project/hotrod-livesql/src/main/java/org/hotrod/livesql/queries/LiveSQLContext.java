package org.hotrod.livesql.queries;

import java.sql.Connection;

import javax.sql.DataSource;

import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.queries.typesolver.RuntimeTypeSolver;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class LiveSQLContext {

  private LiveSQLDialect liveSQLDialect;
  private DataSource dataSource;
  private RuntimeTypeSolver typeSolver;
  private DynamicSQL dynamicSQL;
  private String qualifier;

  public LiveSQLContext(LiveSQLDialect liveSQLDialect, DataSource dataSource, RuntimeTypeSolver typeSolver,
      DynamicSQL dynamicSQL, String qualifier) {
    this.liveSQLDialect = liveSQLDialect;
    this.dataSource = dataSource;
    this.typeSolver = typeSolver;
    this.dynamicSQL = dynamicSQL;
    this.qualifier = qualifier;
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

  public DynamicSQL getDynamicSQL() {
    return dynamicSQL;
  }

  public String getQualifier() {
    return qualifier;
  }

  public Connection getConnection() {
    return DataSourceUtils.getConnection(this.dataSource);
  }

  public void releaseConnection(Connection conn) {
    if (conn != null) {
      DataSourceUtils.releaseConnection(conn, this.dataSource);
    }
  }

}
