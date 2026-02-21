package org.hotrod.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.select.sets.SelectObject;
import org.hotrod.livesql.util.LoggingUtil;
import org.hotrod.utils.Separator;

public class InsertObject {

  private TableOrView<?> into;
  private List<EntityColumnMetadata> columns;
  private List<ComparableExpression> values;
  private SelectObject<?> select;

  InsertObject() {
    super();
  }

  void setInto(final TableOrView<?> into) {
    this.into = into;
  }

  void setColumns(final List<EntityColumnMetadata> columns) {
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
    return this.execute(context, LiveSQLLogging.NO_LOGGING);
  }

  public int execute(final LiveSQLContext context, LiveSQLLogging loggingAdapter) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);

    LoggingUtil.logQuery(q, loggingAdapter);

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
      Separator sep = new Separator(", ");
      for (EntityColumnMetadata c : this.columns) {
        w.write(sep.render());
        w.write(w.getSQLDialect().canonicalToNatural(c.getCanonicalName()));
      }
      w.write(")");
    }

    if (this.values != null) { // insert using values

      w.write("\nVALUES (");
      for (int i = 0; i < this.values.size(); i++) {
        ComparableExpression e = this.values.get(i);
        Shield.renderTo(e, w);
        if (i < this.values.size() - 1) {
          w.write(", ");
        }
      }
      w.write(")");

    } else { // insert from query

      Set<SelectObject<?>> compiling = new HashSet<>();
      this.select.compileColumns(compiling);

      w.write("\n");
      this.select.renderTo(w, null, false);

    }

    LiveSQLPreparedQuery pq = w.getPreparedQuery(null, false);
    return pq;
  }

}
