package org.hotrod.runtime.livesql.queries;

import java.util.LinkedHashMap;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLStructure;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class InsertObject extends QueryObject {

  private TableOrView into;
  private List<Column> columns;
  private List<Expression> values;
  private SelectObject<?> select;

  InsertObject() {
    super();
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

  void setSelect(final SelectObject<?> select) {
    this.select = select;
  }

  public String getPreview(final LiveSQLContext context) {
    LiveSQLStructure pq = this.prepareQuery(context);
    return pq.render();
  }

  public void execute(final LiveSQLContext context) {
    LiveSQLStructure q = this.prepareQuery(context);
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    context.getLiveSQLMapper().insert(parameters);
  }

  private LiveSQLStructure prepareQuery(final LiveSQLContext context) {
    QueryWriter w = new QueryWriter(context.getLiveSQLDialect());
    w.write("INSERT INTO ");
    w.write(context.getLiveSQLDialect().canonicalToNatural(this.into));

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
