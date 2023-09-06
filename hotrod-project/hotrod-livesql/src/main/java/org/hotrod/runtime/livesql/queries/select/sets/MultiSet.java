package org.hotrod.runtime.livesql.queries.select.sets;

public interface MultiSet<R> {

  void setParentOperator(final SetOperator<R> parent);

  SetOperator<R> getParentOperator();

}
