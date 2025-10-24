package org.hotrod.livesql.queries.select;

import java.util.Set;

import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.SQLMetaExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.select.sets.SelectObject;
import org.hotrod.livesql.util.ToString;

public abstract class TableExpression {

  protected abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  protected abstract Name getName();

  protected abstract void renderColumns(Set<SelectObject<?>> compiling);

  protected abstract void renderTo(QueryWriter w);

  protected abstract SQLMetaExpression star();

  protected abstract void log(ToString t);

}
