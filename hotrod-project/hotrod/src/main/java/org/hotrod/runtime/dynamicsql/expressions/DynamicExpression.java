package org.hotrod.runtime.dynamicsql.expressions;

import java.util.List;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;
import org.hotrodorm.hotrod.utils.ListWriter;
import org.hotrodorm.hotrod.utils.SUtils;

public abstract class DynamicExpression {

  private static final Logger log = LogManager.getLogger(DynamicExpression.class);

  private static final int JEXL_CACHE_MAX_EXPRESSIONS = 200;

  protected static JexlEngine JEXL_ENGINE = new JexlBuilder().cache(JEXL_CACHE_MAX_EXPRESSIONS).strict(true)
      .debug(false).silent(false).create();

  abstract EvaluationFeedback evaluate(StringBuilder out, DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException;

  public final String evaluate(final DynamicSQLParameters variables) throws DynamicSQLEvaluationException {
    log.debug("init");
    try {
      StringBuilder sb = new StringBuilder();
      this.evaluate(sb, variables);
      return sb.toString();
    } catch (RuntimeException e) {
      throw new DynamicSQLEvaluationException("Could not evaluate expression: " + e.getMessage());
    }
  }

  public abstract List<Object> getConstructorParameters();

  public final String renderConstructor(final int margin) {
    // log.info(" * class " + this.getClass().getName());
    String indent = SUtils.getFiller(' ', margin);
    String nextIndent = SUtils.getFiller(' ', margin + 2);
    StringBuilder sb = new StringBuilder();

    sb.append(indent);
    sb.append("new " + this.getClass().getName() + "(");

    List<Object> params = this.getConstructorParameters();

    if (params != null && !params.isEmpty()) {
      ListWriter lw = new ListWriter("", "\n", "", ",", ",", "");
      for (Object obj : params) {
        if (obj == null) {
          lw.add(nextIndent + "null");
        } else {
          try {
            @SuppressWarnings("unchecked")
            List<String> ls = (List<String>) obj;
            // log.info("ls=" + ls);
            ListWriter pw = new ListWriter(", ");
            for (String s : ls) {
              if (s == null) {
                pw.add("null");
              } else {
                pw.add("\"" + SUtils.escapeJavaString(s) + "\"");
              }
            }
            lw.add(nextIndent + pw.toString());
          } catch (ClassCastException e1) {
            try {
              DynamicExpression expr = (DynamicExpression) obj;
              lw.add(expr.renderConstructor(margin + 2));
            } catch (ClassCastException e2) {
              throw new IllegalArgumentException(
                  "Could not render constructor for parameter of class: " + obj.getClass().getName());
            }
          }
        }
      }
      sb.append(lw.toString());
      sb.append("\n" + indent);
    }

    sb.append(")");

    return sb.toString();

  }

}
