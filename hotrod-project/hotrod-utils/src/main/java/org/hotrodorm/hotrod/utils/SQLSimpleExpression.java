package org.hotrodorm.hotrod.utils;

import java.util.HashMap;
import java.util.Map;

import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class SQLSimpleExpression implements SQLLogicalExpression {
  private static long count = 0;

  private static synchronized String getParameterName() {
    return "p" + (count++);
  }

  public static enum SimpleExpressionOperator {
    GREATER_THAN, GREATER_OR_EQUAL, LOWER_THAN, LOWER_OR_EQUAL, EQUALS, NOT_EQUAL, LIKE, NOT_LIKE, IN, NOT_IN, IS_NULL,
    NOT_IS_NULL, BETWEEN, NOT_BETWEEN;
  }

  private String renderedExpression;

  private Map<String, Object> parameters = new HashMap<String, Object>();

  public SQLSimpleExpression(SQLField sqlField, SimpleExpressionOperator operator, Object... values) {
    doTheRendering(sqlField.render(), operator, values);
  }

  public SQLSimpleExpression(SQLField sqlField, SQLField sqlField2, SimpleExpressionOperator operator) {
    doTheRendering(sqlField.render(), sqlField2.render(), operator);
  }

  @Override
  public String render() {
    return this.renderedExpression;
  }

  private void doTheRendering(String sqlFieldname, String sqlFieldname2, SimpleExpressionOperator operator) {
    switch (operator) {
    case EQUALS:
      this.renderedExpression = (sqlFieldname + " = " + sqlFieldname2);
      break;
    case NOT_EQUAL:
      this.renderedExpression = (sqlFieldname + " != " + sqlFieldname2);
      break;
    case GREATER_THAN:
      this.renderedExpression = (sqlFieldname + " > " + sqlFieldname2);
      break;
    case GREATER_OR_EQUAL:
      this.renderedExpression = (sqlFieldname + " >= " + sqlFieldname2);
      break;
    case LOWER_THAN:
      this.renderedExpression = (sqlFieldname + " < " + sqlFieldname2);
      break;
    case LOWER_OR_EQUAL:
      this.renderedExpression = (sqlFieldname + " <= " + sqlFieldname2);
      break;
    case LIKE:
      this.renderedExpression = (sqlFieldname + " LIKE " + sqlFieldname2 + " ESCAPE '\\'");
      break;
    case NOT_LIKE:
      this.renderedExpression = (sqlFieldname + " NOT LIKE " + sqlFieldname2 + " ESCAPE '\\'");
      break;
    default:
      throw new RuntimeException("Invalid SimpleExpressionOperator in this context!");
    }

  }

  private void doTheRendering(String sqlFieldname, SimpleExpressionOperator operator, Object... values) {
    StringBuffer sb = new StringBuffer();
    String parameter;
    switch (operator) {
    case EQUALS:
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      this.renderedExpression = (sqlFieldname + " = " + ":" + parameter);
      break;
    case NOT_EQUAL:
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      this.renderedExpression = (sqlFieldname + " != " + ":" + parameter);
      break;
    case GREATER_THAN:
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      this.renderedExpression = (sqlFieldname + " > " + ":" + parameter);
      break;
    case GREATER_OR_EQUAL:
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      this.renderedExpression = (sqlFieldname + " >= " + ":" + parameter);
      break;
    case LOWER_THAN:
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      this.renderedExpression = (sqlFieldname + " < " + ":" + parameter);
      break;
    case LOWER_OR_EQUAL:
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      this.renderedExpression = (sqlFieldname + " <= " + ":" + parameter);
      break;
    case LIKE:
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      this.renderedExpression = (sqlFieldname + " LIKE " + ":" + parameter + " ESCAPE '\\'");
      break;
    case NOT_LIKE:
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      this.renderedExpression = (sqlFieldname + " NOT LIKE " + ":" + parameter + " ESCAPE '\\'");
      break;
    case IS_NULL:
      this.renderedExpression = (sqlFieldname + " IS NULL");
      break;
    case NOT_IS_NULL:
      this.renderedExpression = (sqlFieldname + " IS NOT NULL");
      break;
    case IN:
    case NOT_IN: {
      sb.append(sqlFieldname + (operator.equals(SimpleExpressionOperator.NOT_IN) ? " NOT" : "") + " IN (");
      ListWriter lp = new ListWriter(", ");
      for (Object v : values) {
        parameter = SQLSimpleExpression.getParameterName();
        this.parameters.put(parameter, v);
        lp.add(":" + parameter);
      }
      sb.append(lp.toString() + ")");
      this.renderedExpression = (sb.toString());
      break;
    }
    case BETWEEN:
    case NOT_BETWEEN:
      sb.append(sqlFieldname + (operator.equals(SimpleExpressionOperator.NOT_BETWEEN) ? " NOT" : "") + " BETWEEN ");
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[0]);
      sb.append(":" + parameter + " AND ");
      parameter = SQLSimpleExpression.getParameterName();
      this.parameters.put(parameter, values[1]);
      sb.append(":" + parameter);
      this.renderedExpression = (sb.toString());
      break;
    }
  }

  @Override
  public String toString() {
    return this.render();
  }

  @Override
  public Map<String, Object> getParameters() {
    return this.parameters;
  }
}
