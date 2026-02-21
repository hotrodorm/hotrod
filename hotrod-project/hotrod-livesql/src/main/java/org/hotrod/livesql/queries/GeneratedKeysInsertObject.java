package org.hotrod.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.keys.GeneratedKeysInsertExecutor;
import org.hotrod.livesql.queries.select.sets.SelectObject;
import org.hotrod.livesql.util.LoggingUtil;
import org.hotrod.utils.SUtil;
import org.hotrod.utils.Separator;

public class GeneratedKeysInsertObject<T> {

  private static final Logger log = Logger.getLogger(GeneratedKeysInsertObject.class.getName());

  private GeneratedKeysInsertExecutor<T> executor;
  private TableOrView<?> into;
  private List<EntityColumnMetadata> columns;
  private List<ComparableExpression> values;
  private SelectObject<?> select;

  GeneratedKeysInsertObject(GeneratedKeysInsertExecutor<T> executor) {
    super();
    log.fine("init");
    this.executor = executor;
  }

  void setInto(final TableOrView<?> into) {
    this.into = into;
  }

  void setColumns(final List<EntityColumnMetadata> columns) {
    this.columns = columns;
  }

  void setValues(final List<ComparableExpression> values) {
    this.values = values;
    this.select = null;
  }

  void setSelect(final SelectObject<?> select) {
    this.select = select;
    this.values = null;
  }

  public String getPreview(final LiveSQLContext context, boolean includeParameters) {
    LiveSQLPreparedQuery pq = this.prepareQuery(context);
    return pq.getPreview(includeParameters);
  }

  public T executeOne(final LiveSQLContext context) {
    return this.executeOne(context, LiveSQLLogging.NO_LOGGING);
  }

  public T executeOne(final LiveSQLContext context, LiveSQLLogging loggingAdapter) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);

    LoggingUtil.logQuery(q, loggingAdapter);

    try (Connection conn = context.getDataSource().getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(q.getSQL())) {
        this.executor.applyParameters(q, ps);
        T key = this.executor.executeOne(q, conn);
        return key;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public InsertResult<T> executeList(final LiveSQLContext context) {
    return this.executeList(context, LiveSQLLogging.NO_LOGGING);
  }

  public InsertResult<T> executeList(final LiveSQLContext context, LiveSQLLogging loggingAdapter) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);

    LoggingUtil.logQuery(q, loggingAdapter);

    try (Connection conn = context.getDataSource().getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(q.getSQL())) {
        this.executor.applyParameters(q, ps);
        InsertResult<T> r = this.executor.executeList(q, conn);
        return r;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {
    QueryWriter w = new QueryWriter(context);

    InsertSelectRenderingEdits edits = this.executor.getInsertSelectEdits(this.columns);

    w.write("INSERT INTO ");
    w.write(context.getLiveSQLDialect().canonicalToNatural(this.into));

    if (this.columns != null) {
      w.write(" (");
      Separator sep = new Separator(", ");
      if (edits.getPrependInsertColumn() != null) {
        w.write(sep.render());
        w.write(w.getSQLDialect().canonicalToNatural(edits.getPrependInsertColumn().getCanonicalName()));
      }
      for (EntityColumnMetadata c : this.columns) {
        w.write(sep.render());
        w.write(w.getSQLDialect().canonicalToNatural(c.getCanonicalName()));
      }
      w.write(")");
    }

    if (edits != null && !SUtil.isEmpty(edits.getOutputClausePrefix())) {
      w.write(" " + edits.getOutputClausePrefix());
      w.write(w.getSQLDialect().canonicalToNatural(edits.getPrependInsertColumn().getCanonicalName()));
    }

    if (this.values != null) { // insert using values

      w.write("\nVALUES (");
      Separator sep = new Separator(", ");

      if (edits != null && edits.getPrependValue() != null) {
        w.write(sep.render());
        Shield.renderTo(edits.getPrependValue(), w);
      }

      for (int i = 0; i < this.values.size(); i++) {
        w.write(sep.render());
        ComparableExpression e = this.values.get(i);
        Shield.renderTo(e, w);
      }
      w.write(")");

    }

    if (this.select != null) { // insert from query
      Set<SelectObject<?>> compiling = new HashSet<>();
      this.select.compileColumns(compiling);
      w.write("\n");
      this.select.renderTo(w, edits, false);
    }

    LiveSQLPreparedQuery pq = w.getPreparedQuery(null, false);

    return pq;
  }

  public static class InsertSelectRenderingEdits {

    private EntityColumnMetadata prependInsertColumn;
    private ComparableExpression prependValue;
    private String outputClausePrefix; // OUTPUT INSERTED. -> OUTPUT INSERTED.id

    private InsertSelectRenderingEdits(EntityColumnMetadata prependInsertColumn, ComparableExpression prependValue,
        String outputClausePrefix) {
      this.prependInsertColumn = prependInsertColumn;
      this.prependValue = prependValue;
      this.outputClausePrefix = outputClausePrefix;
    }

    public static InsertSelectRenderingEdits of(EntityColumnMetadata prependInsertColumn,
        ComparableExpression prependValue, String outputClausePrefix) {
      return new InsertSelectRenderingEdits(prependInsertColumn, prependValue, outputClausePrefix);
    }

    public final EntityColumnMetadata getPrependInsertColumn() {
      return prependInsertColumn;
    }

    public final ComparableExpression getPrependValue() {
      return prependValue;
    }

    public final String getOutputClausePrefix() {
      return outputClausePrefix;
    }

  }

}
