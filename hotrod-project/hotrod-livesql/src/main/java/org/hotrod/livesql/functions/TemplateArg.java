package org.hotrod.livesql.functions;

import java.util.Iterator;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;

public class TemplateArg extends TemplateSegment {

  private LiveSQLFunctionTemplate template;

  private TemplateArg(LiveSQLFunctionTemplate template) {
    this.template = template;
  }

  static TemplateArg of(LiveSQLFunctionTemplate template) {
    return new TemplateArg(template);
  }

  @Override
  void prepare(Iterator<ComparableExpression> it, PreparedFunction preparingFunction) {
    if (!it.hasNext()) {
      throw new LiveSQLException("Not enough arguments for the function template: " + this.template.getName());
    }
    ComparableExpression p = it.next();
    preparingFunction.add(PreparedArg.of(p));
  }

}