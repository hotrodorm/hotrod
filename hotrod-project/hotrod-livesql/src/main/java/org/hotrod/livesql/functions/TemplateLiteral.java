package org.hotrod.livesql.functions;

import java.util.Iterator;

import org.hotrod.livesql.expressions.ComparableExpression;

public class TemplateLiteral extends TemplateSegment {

  private static final long serialVersionUID = 1L;

  private String text;

  private TemplateLiteral(String text) {
    this.text = text;
  }

  static TemplateLiteral of(String text) {
    return new TemplateLiteral(text);
  }

  @Override
  public void prepare(Iterator<ComparableExpression> it, PreparedFunction preparingFunction) {
    preparingFunction.add(PreparedLiteral.of(this.text));
  }

}