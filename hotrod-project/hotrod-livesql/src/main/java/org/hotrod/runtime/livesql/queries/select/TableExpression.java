package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.subqueries.EmergingColumn;

public abstract class TableExpression {

  protected abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  protected abstract Name getName();

  protected abstract List<EmergingColumn> assembleColumns();

  protected abstract void renderTo(QueryWriter w);

}
