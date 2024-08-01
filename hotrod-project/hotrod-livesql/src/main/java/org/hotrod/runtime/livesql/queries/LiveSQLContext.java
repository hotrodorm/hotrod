package org.hotrod.runtime.livesql.queries;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;

public class LiveSQLContext {

  private LiveSQLDialect liveSQLDialect;
  private SqlSession sqlSession;
  private LiveSQLMapper liveSQLMapper;
  private boolean usePlainJDBC;
  private DataSource dataSource;
  private TypeSolver typeSolver;

  public LiveSQLContext(final LiveSQLDialect liveSQLDialect, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper, final boolean usePlainJDBC, final DataSource dataSource, final TypeSolver typeSolver) {
    this.liveSQLDialect = liveSQLDialect;
    this.sqlSession = sqlSession;
    this.liveSQLMapper = liveSQLMapper;
    this.usePlainJDBC = usePlainJDBC;
    this.dataSource = dataSource;
    this.typeSolver = typeSolver;
  }

  public LiveSQLDialect getLiveSQLDialect() {
    return liveSQLDialect;
  }

  public SqlSession getSQLSession() {
    return sqlSession;
  }

  public LiveSQLMapper getLiveSQLMapper() {
    return liveSQLMapper;
  }

  public boolean usePlainJDBC() {
    return usePlainJDBC;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public TypeSolver getTypeSolver() {
    return typeSolver;
  }

}
