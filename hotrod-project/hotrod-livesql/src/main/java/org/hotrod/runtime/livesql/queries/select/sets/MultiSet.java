package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public interface MultiSet<R> {

  void setParentOperator(SetOperator<R> parent);

  SetOperator<R> getParentOperator();

  void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  void renderTo(QueryWriter w);

  String getPreview(LiveSQLContext context);

  List<R> execute(LiveSQLContext context);

  Cursor<R> executeCursor(LiveSQLContext context);

}
