package org.hotrod.runtime.livesql.metadata;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.Expression;

public abstract class View extends TableOrView {

  public View(final Name catalog, final Name schema, final Name name, final String type, final String alias) {
    super(catalog, schema, name, type, alias);
  }

//  @Override
//  protected List<Expression> getExpandedColumns() {
//    return super.columns.stream().map(c -> (Expression) c).collect(Collectors.toList());
//  }
//
//  @Override
//  protected void assembleColumns() {
//    // Nothing to do
//  }
//
//  @Deprecated
//  @Override
//  protected void computeQueryColumns() {
//    // Nothing to do
//  }

}
