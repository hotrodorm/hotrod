package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrodorm.hotrod.utils.SUtil;

public interface ExecutableSelect<R> {

  void renderTo(final QueryWriter w);

  List<R> execute();

  Cursor<R> executeCursor();

  String getPreview();

  void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag);

  void designateAliases(final AliasGenerator ag);

  List<ResultSetColumn> listColumns() throws IllegalAccessException;

  default void renderTree(final StringBuilder sb, final int level) {
    sb.append(SUtil.getFiller(". ", level) + "+ [subquery] " + this.getClass().getName() + "\n");
  }

}
