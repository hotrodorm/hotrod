package org.hotrod.livesql.queries.select;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class CriteriaPhase<T> implements EntitySelect<T> {

  protected LiveSQLContext context;
  protected FlatSelectObject<T> select;
  protected RowReader<T> rowReader;
  protected LiveSQLLogging logger;

  protected CriteriaPhase(final LiveSQLContext context, final FlatSelectObject<T> select, RowReader<T> rowReader,
      LiveSQLLogging logger) {
    this.context = context;
    this.select = select;
    this.rowReader = rowReader;
    this.logger = logger;
  }

  protected CriteriaPhase(CriteriaPhase<T> previous) {
    this.context = previous.context;
    this.select = previous.select;
    this.rowReader = previous.rowReader;
    this.logger = previous.logger;
  }

  // next phases

  // execute

  public final List<T> execute() {
    return this.select.execute(this.context, this.rowReader, this.logger);
  }

  public final Cursor<T> executeCursor() throws SQLException {
    return this.select.executeCursor(this.context, this.rowReader, null);
  }

  public final Cursor<T> executeCursor(int fetchSize) throws SQLException {
    return this.select.executeCursor(this.context, this.rowReader, fetchSize);
  }

  public final T executeOne() {
    return this.select.executeOne(this.context, this.rowReader);
  }

  // rendering

  protected void renderTo(QueryWriter w) {
    this.select.renderTo(w, null);
  }

  @Override
  public String getPreview() {
    return this.select.getPreview(this.context, false);
  }

  @Override
  public String getPreview(boolean includeParameters) {
    return this.select.getPreview(this.context, includeParameters);
  }

}
