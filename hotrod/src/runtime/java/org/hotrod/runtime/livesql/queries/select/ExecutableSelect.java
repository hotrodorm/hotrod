package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public interface ExecutableSelect<R> {

  void renderTo(final QueryWriter w);

  List<R> execute();

  String getPreview();

  void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag);

  void designateAliases(final AliasGenerator ag);

}
