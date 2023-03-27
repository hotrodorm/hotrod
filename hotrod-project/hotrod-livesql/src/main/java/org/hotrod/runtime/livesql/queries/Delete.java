package org.hotrod.runtime.livesql.queries;

import java.util.LinkedHashMap;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLStructure;

public class Delete {

  private LiveSQLDialect sqlDialect;
  @SuppressWarnings("unused")
  private SqlSession sqlSession;
  private LiveSQLMapper liveSQLMapper;

  private TableOrView from;
  private Predicate wherePredicate;

  Delete(final LiveSQLDialect sqlDialect, final SqlSession sqlSession, final LiveSQLMapper liveSQLMapper) {
    this.sqlDialect = sqlDialect;
    this.sqlSession = sqlSession;
    this.liveSQLMapper = liveSQLMapper;
  }

  void setFrom(final TableOrView from) {
    this.from = from;
  }

  void setWherePredicate(final Predicate predicate) {
    this.wherePredicate = predicate;
  }

  public String getPreview() {
    LiveSQLStructure pq = this.prepareQuery();
    return pq.render();
  }

  public void execute() {
    LiveSQLStructure q = this.prepareQuery();
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    this.liveSQLMapper.delete(parameters);
  }

  private LiveSQLStructure prepareQuery() {
    QueryWriter w = new QueryWriter(this.sqlDialect);
    w.write("DELETE FROM ");
    w.write(this.sqlDialect.getIdentifierRenderer().renderSQLObjectName(this.from));
    if (this.wherePredicate != null) {
      w.write("\nWHERE ");
      this.wherePredicate.renderTo(w);
    }
    LiveSQLStructure pq = w.getPreparedQuery();
    return pq;
  }

}
