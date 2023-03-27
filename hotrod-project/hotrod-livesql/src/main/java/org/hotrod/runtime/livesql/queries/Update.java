package org.hotrod.runtime.livesql.queries;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLStructure;

public class Update {

  private LiveSQLDialect sqlDialect;
  @SuppressWarnings("unused")
  private SqlSession sqlSession;
  private LiveSQLMapper liveSQLMapper;

  private TableOrView tableOrView;
  private List<Assignment> sets = new ArrayList<>();
  private Predicate wherePredicate;

  Update(final LiveSQLDialect sqlDialect, final SqlSession sqlSession, final LiveSQLMapper liveSQLMapper) {
    this.sqlDialect = sqlDialect;
    this.sqlSession = sqlSession;
    this.liveSQLMapper = liveSQLMapper;
  }

  void setTableOrView(final TableOrView from) {
    this.tableOrView = from;
  }

  void addSet(final Column c, final Expression e) {
    this.sets.add(new Assignment(c, e));
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
    this.liveSQLMapper.update(parameters);
  }

  private LiveSQLStructure prepareQuery() {
    QueryWriter w = new QueryWriter(this.sqlDialect);
    w.write("UPDATE ");
    w.write(this.sqlDialect.getIdentifierRenderer().renderSQLObjectName(this.tableOrView));
    w.write("\nSET ");
    for (int i = 0; i < this.sets.size(); i++) {
      Assignment s = this.sets.get(i);
      s.getColumn().renderTo(w);
      w.write(" = ");
      s.getExpression().renderTo(w);
      if (i < this.sets.size() - 1) {
        w.write(",\n    ");
      }
    }
    if (this.wherePredicate != null) {
      w.write("\nWHERE ");
      this.wherePredicate.renderTo(w);
    }
    LiveSQLStructure pq = w.getPreparedQuery();
    return pq;
  }

  private static class Assignment {
    private Column c;
    private Expression e;

    public Assignment(Column c, Expression e) {
      super();
      this.c = c;
      this.e = e;
    }

    public Column getColumn() {
      return c;
    }

    public Expression getExpression() {
      return e;
    }

  }

}
