package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.Query;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;

public interface Select<R> extends Query {

  List<R> execute();

  Cursor<R> executeCursor();

  CombinedSelectObject<R> getCombinedSelect();

}
