package org.hotrod.runtime.livesql.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public abstract class View extends TableOrView {

  public View(final String catalog, final String schema, final String name, final String type, final String alias) {
    super(catalog, schema, name, type, alias);
  }

  @Override
  public List<ResultSetColumn> getColumns() throws IllegalAccessException {
    return super.columns.stream().map(c -> (ResultSetColumn) c).collect(Collectors.toList());
  }

}
