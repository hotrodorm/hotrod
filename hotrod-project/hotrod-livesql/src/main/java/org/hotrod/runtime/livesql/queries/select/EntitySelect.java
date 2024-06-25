package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.Query;

public interface EntitySelect<R> extends Query {

//  void renderTo(final QueryWriter w);

  List<R> execute();

  R executeOne();

  Cursor<R> executeCursor();

}
