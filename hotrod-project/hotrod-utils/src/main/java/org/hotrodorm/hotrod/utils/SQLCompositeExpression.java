package org.hotrodorm.hotrod.utils;

import java.util.HashMap;
import java.util.Map;

import org.hotrodorm.hotrod.utils.SQLSimpleExpression.SimpleExpressionOperator;

public class SQLCompositeExpression implements SQLLogicalExpression {
  public static enum CompositeExpressionOperator {
    AND, OR
  }

  private Map<String, Object> parameters = new HashMap<String, Object>();

  private String renderedExpression;

  public SQLCompositeExpression(CompositeExpressionOperator operator, SQLLogicalExpression... expressions) {
    doTheRendering(operator, expressions);
  }

  @Deprecated
  public SQLCompositeExpression(SQLLogicalExpression expression, CompositeExpressionOperator operator,
      SQLLogicalExpression expression2) {
    doTheRendering(operator, expression, expression2);
  }

  @Override
  public String render() {
    return this.renderedExpression;
  }

  private void doTheRendering(CompositeExpressionOperator operator, SQLLogicalExpression... expressions) {
    String op = (operator.equals(CompositeExpressionOperator.AND) ? " AND " : " OR ");
    StringBuffer sb = new StringBuffer(expressions[0].render());
    parameters = expressions[0].getParameters();
    for (int i = 1; i < expressions.length; i++) {
      sb.append(op + expressions[i].render());
      parameters.putAll(expressions[i].getParameters());
    }
    this.renderedExpression = "(" + sb.toString() + ")";
  }

  @Override
  public Map<String, Object> getParameters() {
    return this.parameters;
  }

  public static void main(String[] args) {
    SQLSimpleExpression a = new SQLSimpleExpression(new SQLField(new SQLTable("T1", "TA1"), "T1.A"),
        SimpleExpressionOperator.LIKE, "dalt%");

    System.out.println(a.render());

    SQLSimpleExpression b = new SQLSimpleExpression(new SQLField("B"), SimpleExpressionOperator.EQUALS, "1");
    SQLSimpleExpression c = new SQLSimpleExpression(new SQLField("C"), SimpleExpressionOperator.IN, "1", "2", "3", "4");
    SQLSimpleExpression d = new SQLSimpleExpression(new SQLField("D"), SimpleExpressionOperator.NOT_EQUAL, "8");
    SQLSimpleExpression e = new SQLSimpleExpression(new SQLField("E"), SimpleExpressionOperator.LOWER_OR_EQUAL, "66");
    SQLSimpleExpression f = new SQLSimpleExpression(new SQLField("F"), SimpleExpressionOperator.LOWER_THAN, "7");

    System.out.println(b.render());
    System.out.println(c.render());
    System.out.println(d.render());
    System.out.println(e.render());
    System.out.println(f.render());
    System.out.println();

    SQLCompositeExpression composite = new SQLCompositeExpression(a, CompositeExpressionOperator.AND, b);
    SQLCompositeExpression composite2 = new SQLCompositeExpression(e, CompositeExpressionOperator.AND, f);
    System.out.println(composite.render());
    composite = new SQLCompositeExpression(composite, CompositeExpressionOperator.OR, c);
    System.out.println(composite.render());
    composite = new SQLCompositeExpression(composite, CompositeExpressionOperator.AND, composite2);
    System.out.println(composite.render());
    composite = new SQLCompositeExpression(CompositeExpressionOperator.AND, a, b, c, d, e, f);
    System.out.println(composite.render());

  }

  @Override
  public String toString() {
    return this.render();
  }
}
