package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;

public interface ExecutableCriteriaSelect<R> {

  void renderTo(final QueryWriter w);

  List<R> execute();

  Cursor<R> executeCursor();

  String getPreview();

}
