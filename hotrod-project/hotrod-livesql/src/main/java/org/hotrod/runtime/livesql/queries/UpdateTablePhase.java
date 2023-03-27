package org.hotrod.runtime.livesql.queries;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class UpdateTablePhase {

  // Properties

  private Update update;

  // Constructor

  public UpdateTablePhase(final LiveSQLDialect sqlDialect, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper, final TableOrView tableOrView) {
    this.update = new Update(sqlDialect, sqlSession, liveSQLMapper);
    this.update.setTableOrView(tableOrView);
  }

  // Next stages

  public UpdateSetPhase set(final Column column, final Expression expression) {
    this.update.addSet(column, expression);
    return new UpdateSetPhase(this.update);
  }

}
