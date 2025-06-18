package org.hotrod.livesql.queries.select;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.WrappingColumn;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;

public abstract class TableExpression {

  protected abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  protected abstract Name getName();

  protected abstract List<Expression> assembleColumns();

  protected abstract void renderTo(QueryWriter w);

  protected abstract WrappingColumn star();

}
