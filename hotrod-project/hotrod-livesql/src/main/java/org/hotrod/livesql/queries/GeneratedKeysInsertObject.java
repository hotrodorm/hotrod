package org.hotrod.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.keys.GeneratedKeysInsertExecutor;
import org.hotrod.livesql.util.LoggingUtil;
import org.hotrod.utils.Separator;

public class GeneratedKeysInsertObject<T> {

  private static final Logger log = Logger.getLogger(GeneratedKeysInsertObject.class.getName());

  private GeneratedKeysInsertExecutor<T> executor;
  private TableOrView<?> into;
  private List<EntityColumn> columns;
  private List<ComparableExpression> values;
//  private BaseSelectObject<?> select;

  GeneratedKeysInsertObject(GeneratedKeysInsertExecutor<T> executor) {
    super();
    this.executor = executor;
  }

  void setInto(final TableOrView<?> into) {
    this.into = into;
  }

  void setColumns(final List<EntityColumn> columns) {
    this.columns = columns;
  }

  void setValues(final List<ComparableExpression> values) {
    this.values = values;
  }

//  void setSelect(final BaseSelectObject<?> select) {
//    this.select = select;
//  }

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

  private LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {
    log.info("*** Preparing QUERY");
    QueryWriter w = new QueryWriter(context);
    w.write("INSERT INTO ");
    w.write(context.getLiveSQLDialect().canonicalToNatural(this.into));

    List<EntityColumn> preparedColumns = new ArrayList<>();
    List<ComparableExpression> preparedValues = new ArrayList<>();

    this.executor.validateAndPrepareInsertColumns(this.columns, this.values, preparedColumns, preparedValues);
    log.info("* executor=" + this.executor + " preparedColumns[" + preparedColumns.size() + "]");

    if (preparedColumns != null) {
      w.write(" (");
      Separator sep = new Separator(", ");
      for (EntityColumn c : preparedColumns) {
        w.write(sep.render());
        w.write(w.getSQLDialect().canonicalToNatural(c.getCanonicalName()));
      }
      w.write(")");
    }

    if (preparedValues != null) { // insert using values

      w.write("\nVALUES (");
      for (int i = 0; i < preparedValues.size(); i++) {
        ComparableExpression e = preparedValues.get(i);
        Shield.renderTo(e, w);
        if (i < preparedValues.size() - 1) {
          w.write(", ");
        }
      }
      w.write(")");

//    } else { // insert from query
//
//      w.write("\n");
//      this.select.renderTo(w);

    }

    LiveSQLPreparedQuery pq = w.getPreparedQuery(null, false);
    log.info("*** Preparing QUERY - COMPLETE");

    return pq;
  }

}
