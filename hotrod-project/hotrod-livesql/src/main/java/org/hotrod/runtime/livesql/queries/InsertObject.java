package org.hotrod.runtime.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.metadata.EntityColumn;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class InsertObject implements QueryObject {

  private TableOrView into;
  private List<EntityColumn> columns;
  private List<ComparableExpression> values;
  private SelectObject<?> select;

  InsertObject() {
    super();
  }

  void setInto(final TableOrView into) {
    this.into = into;
  }

  void setColumns(final List<EntityColumn> columns) {
    this.columns = columns;
  }

  void setValues(final List<ComparableExpression> values) {
    this.values = values;
  }

  void setSelect(final SelectObject<?> select) {
    this.select = select;
  }

  public String getPreview(final LiveSQLContext context, boolean includeParameters) {
    LiveSQLPreparedQuery pq = this.prepareQuery(context);
    return pq.getPreview(includeParameters);
  }

  public int execute(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    try (Connection conn = context.getDataSource().getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(q.getSQL())) {

        // 1. Apply parameters

        int n = 1;
        for (Object obj : q.getParameters().values()) {
          int i = n++;
          ps.setObject(i, obj);
        }

        // 2. Run the query

        int count = ps.executeUpdate();
        return count;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {
    QueryWriter w = new QueryWriter(context);
    w.write("INSERT INTO ");
    w.write(context.getLiveSQLDialect().canonicalToNatural(this.into));

    if (this.columns != null) {
      w.write(" (");
      for (int i = 0; i < this.columns.size(); i++) {
        EntityColumn c = this.columns.get(i);
        Expression expr = (Expression) c;
        Helper.renderTo(expr, w);
        if (i < this.columns.size() - 1) {
          w.write(", ");
        }
      }
      w.write(")");
    }

    if (this.values != null) { // insert using values

      w.write("\nVALUES (");
      for (int i = 0; i < this.values.size(); i++) {
        ComparableExpression e = this.values.get(i);
        Helper.renderTo(e, w);
        if (i < this.values.size() - 1) {
          w.write(", ");
        }
      }
      w.write(")");

    } else { // insert from query

      w.write("\n");
      this.select.renderTo(w);

    }

    LiveSQLPreparedQuery pq = w.getPreparedQuery(null);
    return pq;
  }

}
