package org.hotrod.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.dialects.UpdateRenderer;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.bool.converter.ConvertedEntityColumn;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.util.LoggingUtil;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class UpdateObject {

  private static final Logger log = Logger.getLogger(UpdateObject.class.getName());

  private TableOrView<?, ?> tableOrView;
  private List<Assignment> setters = new ArrayList<>();
  private Predicate wherePredicate;

  UpdateObject() {
    super();
    log.fine("init");
  }

  void setTableOrView(final TableOrView<?, ?> from) {
    this.tableOrView = from;
  }

  void addSetter(final EntityColumn c, final Expression e) {
    this.setters.add(new Assignment(c, e));
  }

  <R, D> void addConvertedColumnSetter(final ConvertedEntityColumn<R, D> c, final D d, TypeConverter<R, D> converter) {
    this.setters.add(new ConvertedAssignment<>(c, d, converter));
  }

  void setWherePredicate(final Predicate predicate) {
    this.wherePredicate = predicate;
  }

  public String getPreview(final LiveSQLContext context, final boolean includeParameters) {
    LiveSQLPreparedQuery pq = this.prepareQuery(context);
    return pq.getPreview(includeParameters);
  }

  public int execute(final LiveSQLContext context) {
    return execute(context, LiveSQLLogging.NO_LOGGING);
  }

  public int execute(final LiveSQLContext context, LiveSQLLogging loggingAdapter) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);

    LoggingUtil.logQuery(q, loggingAdapter);

    Connection conn = null;
    try {
      conn = context.getConnection();
      try (PreparedStatement ps = conn.prepareStatement(q.getSQL())) {

        // 1. Prepare and apply the parameters

        q.applyParameters(ps, conn);

        // 2. Run the query

        int count = ps.executeUpdate();
        return count;
      }
    } catch (SQLException e) {
      throw new LiveSQLException("Could not execute LiveSQL query", e);
    } finally {
      context.releaseConnection(conn);
    }
  }

  private LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {

    if (this.setters.isEmpty()) {
      throw new LiveSQLException("The UPDATE query does not include any column to update.");
    }

    QueryWriter w = new QueryWriter(context);
    w.write("UPDATE ");

    UpdateRenderer ur = context.getLiveSQLDialect().getUpdateRenderer();
    if (ur.removeMainTableAlias()) {
      MDShield.removeAlias(this.tableOrView);
    }

    String renderedAlias = this.tableOrView.getAlias() == null ? null
        : context.getLiveSQLDialect()
            .canonicalToNatural(context.getLiveSQLDialect().naturalToCanonical(this.tableOrView.getAlias()));

    w.write(context.getLiveSQLDialect().canonicalToNatural(this.tableOrView)
        + (renderedAlias != null ? (" " + renderedAlias) : ""));

    w.write("\nSET\n");
    boolean first = true;
    for (int i = 0; i < this.setters.size(); i++) {
      w.write("    ");
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      Assignment s = this.setters.get(i);
      w.write(w.getSQLDialect().canonicalToNatural(s.getColumn().getName()));
      w.write(" = ");
      Shield.renderTo(s.getExpression(), w);
      w.write("\n");
    }

    if (this.wherePredicate != null) {
      w.write("WHERE ");
      Shield.renderTo(this.wherePredicate, w);
    }
    LiveSQLPreparedQuery pq = w.getPreparedQuery(null, false);
    return pq;
  }

  private static class Assignment {

    private EntityColumn c;
    private Expression e;

    public Assignment(final EntityColumn c, final Expression e) {
      this.c = c;
      this.e = e;
    }

    public EntityColumn getColumn() {
      return c;
    }

    public Expression getExpression() {
      return e;
    }

  }

  public static class ConvertedAssignment<R, D> extends Assignment {

    private ConvertedEntityColumn<R, D> c;
    private D d;
    private TypeConverter<R, D> converter;

    public ConvertedAssignment(final ConvertedEntityColumn<R, D> c, final D d, TypeConverter<R, D> converter) {
      super(c, null);
      this.c = c;
      this.d = d;
      this.converter = converter;
    }

    public ConvertedEntityColumn<R, D> getColumn() {
      return c;
    }

    public R getRawValue(Connection conn) {
      return this.converter.encode(d, conn);
    }

  }

}
