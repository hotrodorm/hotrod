package org.hotrod.runtime.sql.expressions.analytics;

public class DenseRank extends AnalyticFunction<Number> {

  public DenseRank() {
    super("dense_rank", null);
  }

}
