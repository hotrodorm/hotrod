package org.hotrod.runtime.sql.expressions.analytics;

public class RowNumber extends AnalyticFunction<Number> {

  public RowNumber() {
    super("row_number", null);
  }

}
