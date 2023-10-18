package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

@Deprecated
public abstract class XMultiSet<R> {

  private SetOperator<R> parent;

  public void setParentOperator(SetOperator<R> parent) {
    this.parent = parent;
  }

  public SetOperator<R> getParentOperator() {
    return this.parent;
  }

  public abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  public abstract void renderTo(QueryWriter w);

  public abstract String getPreview(LiveSQLContext context);

  public abstract List<R> execute(LiveSQLContext context);

  public abstract Cursor<R> executeCursor(LiveSQLContext context);

}
