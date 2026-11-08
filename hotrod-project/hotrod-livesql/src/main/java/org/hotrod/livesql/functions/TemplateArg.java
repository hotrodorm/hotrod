package org.hotrod.livesql.functions;

import java.util.Iterator;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;

public class TemplateArg extends TemplateSegment {

  private static final long serialVersionUID = 1L;

  private TemplateArg() {
  }

  static TemplateArg of() {
    return new TemplateArg();
  }

  @Override
  void prepare(Iterator<ComparableExpression> it, PreparedFunction preparingFunction) {
    if (!it.hasNext()) {
      throw new LiveSQLException("Not enough arguments for the function template: " + super.getTemplate().getName());
    }
    ComparableExpression p = it.next();
    preparingFunction.add(PreparedArg.of(p));
  }

}