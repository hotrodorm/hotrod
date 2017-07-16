package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;

public class ForEachExpression extends DynamicExpression {

  private JexlExpression item = null;
  private JexlExpression index = null;
  private JexlExpression collection = null;
  private JexlExpression open = null;
  private JexlExpression separator = null;
  private JexlExpression close = null;

  public ForEachExpression(final String item, final String index, final String collection, final String open,
      final String separator, final String close) {
    this.item = JEXL_ENGINE.createExpression(item);
    this.index = JEXL_ENGINE.createExpression(index);
    this.collection = JEXL_ENGINE.createExpression(collection);
    this.open = JEXL_ENGINE.createExpression(open);
    this.separator = JEXL_ENGINE.createExpression(separator);
    this.close = JEXL_ENGINE.createExpression(close);
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {

    // TODO: implement this!

    return new EvaluationFeedback(false);
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    List<String> stringParams = new ArrayList<String>();
    stringParams.add(this.item.getSourceText());
    stringParams.add(this.index.getSourceText());
    stringParams.add(this.collection.getSourceText());
    stringParams.add(this.open.getSourceText());
    stringParams.add(this.separator.getSourceText());
    stringParams.add(this.close.getSourceText());
    params.add(stringParams);
    return params;
  }

}
