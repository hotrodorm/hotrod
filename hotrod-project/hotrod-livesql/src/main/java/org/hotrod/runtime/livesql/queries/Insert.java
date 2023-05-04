package org.hotrod.runtime.livesql.queries;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLStructure;

public class Insert {

  private LiveSQLDialect sqlDialect;
  @SuppressWarnings("unused")
  private SqlSession sqlSession;
  private LiveSQLMapper liveSQLMapper;

  private TableOrView into;
  private List<Column> columns;
  private List<Expression> values;
  private ExecutableSelect<?> select;

  Insert(final LiveSQLDialect sqlDialect, final SqlSession sqlSession, final LiveSQLMapper liveSQLMapper) {
    this.sqlDialect = sqlDialect;
    this.sqlSession = sqlSession;
    this.liveSQLMapper = liveSQLMapper;
  }

  void setInto(final TableOrView into) {
    this.into = into;
  }

  void setColumns(final List<Column> columns) {
    this.columns = columns;
  }

  void setValues(final List<Expression> values) {
    this.values = values;
  }

  void setSelect(final ExecutableSelect<?> select) {
    this.select = select;
  }

  public String getPreview() {
    LiveSQLStructure pq = this.prepareQuery();
    return pq.render();
  }

  public void execute() {
    LiveSQLStructure q = this.prepareQuery();
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    this.liveSQLMapper.insert(parameters);
  }

  private LiveSQLStructure prepareQuery() {
    QueryWriter w = new QueryWriter(this.sqlDialect);
    w.write("INSERT INTO ");
    w.write(this.sqlDialect.canonicalToNatural(this.into));

    if (this.columns != null) {
      w.write(" (");
      for (int i = 0; i < this.columns.size(); i++) {
        Column c = this.columns.get(i);
        c.renderTo(w);
        if (i < this.columns.size() - 1) {
          w.write(", ");
        }
      }
      w.write(")");
    }

    if (this.values != null) { // insert using values

      w.write("\nVALUES (");
      for (int i = 0; i < this.values.size(); i++) {
        Expression e = this.values.get(i);
        e.renderTo(w);
        if (i < this.values.size() - 1) {
          w.write(", ");
        }
      }
      w.write(")");

    } else { // insert from query

      w.write("\n");
      this.select.renderTo(w);

    }

    LiveSQLStructure pq = w.getPreparedQuery();
    return pq;
  }

}
