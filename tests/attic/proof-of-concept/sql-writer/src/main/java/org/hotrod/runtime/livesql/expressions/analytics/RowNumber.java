package org.hotrod.runtime.livesql.expressions.analytics;

public class RowNumber extends AnalyticFunction<Number> {

  public RowNumber() {
    super("row_number", null);
  }

}
