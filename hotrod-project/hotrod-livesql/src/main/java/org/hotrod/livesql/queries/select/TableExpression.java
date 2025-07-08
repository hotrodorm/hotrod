package org.hotrod.livesql.queries.select;

import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.WrappingColumn;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.util.ToString;

public abstract class TableExpression {

  protected abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  protected abstract Name getName();

  protected abstract void assembleColumns();

  protected abstract void renderTo(QueryWriter w);

  protected abstract WrappingColumn star();

  protected abstract void log(ToString t);

}
