package org.hotrod.runtime.livesql.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
  private String mapperStatement; // DAO.update(values, [t,] predicate)
  private LiveSQLMapper liveSQLMapper; // LiveSQL

  private TableOrView tableOrView;
  private List<Assignment> sets = new ArrayList<>();
  private Predicate wherePredicate;

  private Map<String, Object> extraSets = new HashMap<>();

  Update(final LiveSQLDialect sqlDialect, final SqlSession sqlSession, final LiveSQLMapper liveSQLMapper) {
    this.sqlDialect = sqlDialect;
    this.sqlSession = sqlSession;
    this.mapperStatement = null;
    this.liveSQLMapper = liveSQLMapper;
  }

  Update(final LiveSQLDialect sqlDialect, final SqlSession sqlSession, final String mapperStatement) {
    this.sqlDialect = sqlDialect;
    this.sqlSession = sqlSession;
    this.mapperStatement = mapperStatement;
    this.liveSQLMapper = null;
  }

  void setTableOrView(final TableOrView from) {
    this.tableOrView = from;
  }

  void addSet(final Column c, final Expression e) {
    this.sets.add(new Assignment(c, e));
  }

  void setExtraSets(final Map<String, Object> extraSets) {
    this.extraSets = extraSets;
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
    parameters.put("extraSets", this.extraSets);

    if (this.mapperStatement != null) {
      this.sqlSession.update(this.mapperStatement, q.getConsolidatedParameters());
    } else {
      this.liveSQLMapper.update(parameters);
    }
  }

  private LiveSQLStructure prepareQuery() {
    QueryWriter w = new QueryWriter(this.sqlDialect);
    w.write("UPDATE ");
    w.write(this.sqlDialect.getIdentifierRenderer().renderSQLObjectName(this.tableOrView));

    w.write("\nSET\n");
    boolean first = true;
    for (int i = 0; i < this.sets.size(); i++) {
      w.write("    ");
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      Assignment s = this.sets.get(i);
      s.getColumn().renderTo(w);
      w.write(" = ");
      s.getExpression().renderTo(w);
      w.write("\n");
    }
    for (String colName : this.extraSets.keySet()) {
      w.write("    ");
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      String paramName = w.registerParameter(this.extraSets.get(colName));
      w.write(colName + " = #{" + paramName + "}");
      w.write("\n");
    }

    if (this.wherePredicate != null) {
      w.write("WHERE ");
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
