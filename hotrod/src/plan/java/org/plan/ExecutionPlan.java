package org.plan;

import java.util.Date;
import java.util.Map;

import org.plan.operator.Operator;

public class ExecutionPlan {

  private String queryTag;
  private Date producedAt;

  // stats?

  private String query;
  private Map<String, Object> parameterValues;

  private Operator rootOperator;

  public ExecutionPlan(final String queryTag, final Date producedAt, final String query,
      final Map<String, Object> parameterValues, final Operator rootOperator) {
    this.queryTag = queryTag;
    this.producedAt = producedAt;
    this.query = query;
    this.parameterValues = parameterValues;
    this.rootOperator = rootOperator;
  }

  public String getQueryTag() {
    return queryTag;
  }

  public Date getProducedAt() {
    return producedAt;
  }

  public String getQuery() {
    return query;
  }

  public Map<String, Object> getParameterValues() {
    return parameterValues;
  }

  public Operator getRootOperator() {
    return rootOperator;
  }

}
