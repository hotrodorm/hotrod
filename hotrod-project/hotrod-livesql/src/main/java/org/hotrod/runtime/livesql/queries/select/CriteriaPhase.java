package org.hotrod.runtime.livesql.queries.select;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.data.Cursor;
import org.hotrod.data.RowReader;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public abstract class CriteriaPhase<T> implements EntitySelect<T> {

  protected LiveSQLContext context;
  protected AbstractSelectObject<T> select;
  protected RowReader<T> rowReader;

  protected CriteriaPhase(final LiveSQLContext context, final AbstractSelectObject<T> select, RowReader<T> rowReader) {
    this.context = context;
    this.select = select;
    this.rowReader = rowReader;
  }

  // next phases

  // execute

  public final List<T> execute() {
    return this.select.execute(this.context, this.rowReader);
  }

  public final Cursor<T> executeCursor() throws SQLException {
    return this.select.executeCursor(this.context, this.rowReader, null);
  }

  public final Cursor<T> executeCursor(int fetchSize) throws SQLException {
    return this.select.executeCursor(this.context, this.rowReader, fetchSize);
  }

  public final T executeOne() {
    return this.select.executeOne(this.context);
  }

  // rendering

  protected void renderTo(QueryWriter w) {
    this.select.renderTo(w);
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
