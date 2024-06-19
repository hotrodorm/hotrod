package org.hotrod.runtime.livesql.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public abstract class Table extends TableOrView {

  public Table(final Name catalog, final Name schema, final Name name, final String type, final String alias) {
    super(catalog, schema, name, type, alias);
  }

  @Override
  public List<ResultSetColumn> getColumns() throws IllegalAccessException {
    return super.columns.stream().map(c -> (ResultSetColumn) c).collect(Collectors.toList());
  }

  @Override
  protected void computeQueryColumns() {
    // Nothing to do
  }

}
