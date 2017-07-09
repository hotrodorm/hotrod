package org.hotrod.runtime.dynamicsql.expressions;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public abstract class DynamicSQLExpression {

  private static final int JEXL_CACHE_MAX_EXPRESSIONS = 200;

  protected static JexlEngine JEXL_ENGINE = new JexlBuilder().cache(JEXL_CACHE_MAX_EXPRESSIONS).strict(true)
      .silent(false).create();

  abstract EvaluationFeedback evaluate(StringBuilder out, DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException;

  public final String evaluate(final DynamicSQLParameters variables) throws DynamicSQLEvaluationException {
    StringBuilder sb = new StringBuilder();
    this.evaluate(sb, variables);
    return sb.toString();
  }

}
