package org.hotrod.runtime.livesql.queries;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class DeleteFromPhase implements ExecutableQuery {

  // Properties

  private Delete delete;

  // Constructor

  public DeleteFromPhase(final LiveSQLDialect sqlDialect, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper, final TableOrView from) {
    this.delete = new Delete(sqlDialect, sqlSession, liveSQLMapper);
    this.delete.setFrom(from);
  }

  // Next stages

  public DeleteWherePhase where(final Predicate predicate) {
    return new DeleteWherePhase(this.delete, predicate);
  }

  // Preview

  @Override
  public String getPreview() {
    return this.delete.getPreview();
  }

  // Execute

  @Override
  public void execute() {
    this.delete.execute();
  }

}
