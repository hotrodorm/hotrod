package org.hotrod.runtime.livesql.queries;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;

public class LiveSQLContext {

  private LiveSQLDialect liveSQLDialect;
  private SqlSession sqlSession;
  private LiveSQLMapper liveSQLMapper;

  public LiveSQLContext(final LiveSQLDialect liveSQLDialect, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper) {
    this.liveSQLDialect = liveSQLDialect;
    this.sqlSession = sqlSession;
    this.liveSQLMapper = liveSQLMapper;
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

}
