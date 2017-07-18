package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.jexl3.JexlExpression;
import org.apache.log4j.Logger;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;
import org.hotrod.runtime.util.ListWriter;

public class ForEachExpression extends DynamicExpression {

  // Constants

  private static final Logger log = Logger.getLogger(ForEachExpression.class);

  // Properties

  private String item = null;
  private String index = null;
  private JexlExpression collection = null;
  private String open = null;
  private String separator = null;
  private String close = null;
  private DynamicExpression[] expressions = null;

  public ForEachExpression(final String item, final String index, final String collection, final String open,
      final String separator, final String close, final DynamicExpression... expressions) {
    log.debug("init");
    this.item = item;
    this.index = index;
    log.info("collection=" + collection);
    this.collection = JEXL_ENGINE.createExpression(collection);
    log.info("this.collection=" + this.collection.getSourceText());
    this.open = open;
    this.separator = separator;
    this.close = close;
    this.expressions = expressions;
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {

    ListWriter lw = new ListWriter(this.separator == null ? "" : this.separator);

    Object value = this.collection.evaluate(variables);

    if (value == null) {
      throw new DynamicSQLEvaluationException(
          "ForEach expression could not iterate over the collection '" + this.collection.getSourceText()
              + "'. Its value is null, but an array, an Iterable<?>, or a Map<?,?> was expected.");
    }

    // Iterate over the collection

    try {
      Iterable<?> iterable = (Iterable<?>) value;
      int i = 0;
      for (Object elem : iterable) {
        if (this.index != null) {
          variables.set(this.index, i);
        }
        if (this.item != null) {
          variables.set(this.item, elem);
        }
        evaluateBody(variables, lw);
        i++;
      }
    } catch (ClassCastException e1) {
      try {
        Map<?, ?> map = (Map<?, ?>) value;
        for (Object elemKey : map.keySet()) {
          if (this.index != null) {
            variables.set(this.index, elemKey);
          }
          Object elemValue = map.get(elemKey);
          if (this.item != null) {
            variables.set(this.item, elemValue);
          }
          evaluateBody(variables, lw);
        }
      } catch (ClassCastException e2) {
        try {
          Object[] array = (Object[]) value;
          for (int i = 0; i < array.length; i++) {
            if (this.index != null) {
              variables.set(this.index, i);
            }
            if (this.item != null) {
              variables.set(this.item, array[i]);
            }
            evaluateBody(variables, lw);
          }
        } catch (ClassCastException e3) {
          throw new DynamicSQLEvaluationException(
              "ForEach expression could not iterate over the collection '" + this.collection.getSourceText()
                  + "'. A value of an array, an Iterable<?>, or a Map<?,?> was expected, but evaluated to a value of type '"
                  + value.getClass().getName() + "'.");
        }
      }
    }

    // Clean up temp variables

    if (this.index != null) {
      variables.remove(this.index);
    }
    if (this.item != null) {
      variables.remove(this.item);
    }

    // Produce complete rendering

    if (this.open != null) {
      out.append(this.open);
    }
    out.append(lw.toString());
    if (this.close != null) {
      out.append(this.close);
    }

    return new EvaluationFeedback(false);
  }

  private void evaluateBody(final DynamicSQLParameters variables, final ListWriter lw)
      throws DynamicSQLEvaluationException {
    StringBuilder sb = new StringBuilder();
    for (DynamicExpression expr : this.expressions) {
      log.info("expr=" + expr);
      expr.evaluate(sb, variables);
    }
    lw.add(sb.toString());
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    List<String> stringParams = new ArrayList<String>();
    stringParams.add(this.item);
    stringParams.add(this.index);
    stringParams.add(this.collection.getSourceText());
    stringParams.add(this.open);
    stringParams.add(this.separator);
    stringParams.add(this.close);
    params.add(stringParams);
    params.addAll(Arrays.asList(this.expressions));
    return params;
  }

}
