package org.hotrod.runtime.livesql.queries;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class DeleteWherePhase implements ExecutableQuery {

  // Properties

  private Delete delete;

  // Constructor

  public DeleteWherePhase(final Delete delete, final Predicate predicate) {
    this.delete = delete;
    this.delete.setWherePredicate(predicate);
  }

  public DeleteWherePhase(final TableOrView from, final LiveSQLDialect sqlDialect, final SqlSession sqlSession,
      final String mapperStatement, final Predicate predicate) {
    this.delete = new Delete(sqlDialect, sqlSession, mapperStatement);
    this.delete.setFrom(from);
    this.delete.setWherePredicate(predicate);
  }

  // Next stages

  // Preview

  @Override
  public String getPreview() {
    return this.delete.getPreview();
  }

  // Execute

  public void execute() {
    this.delete.execute();
  }

}
