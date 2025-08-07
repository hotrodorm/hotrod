package org.hotrod.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.dialects.UpdateRenderer;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.TableOrView;

public class UpdateObject {

  private static final Logger log = Logger.getLogger(UpdateObject.class.getName());

  private TableOrView tableOrView;
  private List<Assignment> setters = new ArrayList<>();
  private GeneralBooleanExpression wherePredicate;

  UpdateObject() {
    super();
    log.fine("init");
  }

  void setTableOrView(final TableOrView from) {
    this.tableOrView = from;
  }

  void addSetter(final EntityColumn c, final Expression e) {
    this.setters.add(new Assignment(c, e));
  }

  void setWherePredicate(final GeneralBooleanExpression predicate) {
    this.wherePredicate = predicate;
  }

  public String getPreview(final LiveSQLContext context, final boolean includeParameters) {
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
      w.write(w.getSQLDialect().canonicalToNatural(s.getColumn().getCanonicalName()));
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

}
