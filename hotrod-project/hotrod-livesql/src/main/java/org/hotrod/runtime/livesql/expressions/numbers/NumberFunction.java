package org.hotrod.runtime.livesql.expressions.numbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.InvalidFunctionException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.rendering.Renderer;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrodorm.hotrod.utils.SUtil;

public abstract class NumberFunction extends NumberExpression {

  private String name;
  private String qualifier;
  private boolean includeParenthesis;
  private List<Expression> parameters = new ArrayList<Expression>();

  protected NumberFunction(final String name, final Expression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    if (SUtil.isEmpty(name)) {
      throw new InvalidFunctionException("The function name cannot be empty");
    }
    this.name = name;
    this.qualifier = null;
    this.includeParenthesis = true;
    this.parameters = Arrays.asList(parameters);
    this.parameters.forEach(p -> super.register(p));
  }

  protected NumberFunction(final String name, final String qualifier, final Expression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    if (SUtil.isEmpty(name)) {
      throw new InvalidFunctionException("The function name cannot be empty");
    }
    this.name = name;
    this.qualifier = qualifier;
    this.includeParenthesis = true;
    this.parameters = Arrays.asList(parameters);
    this.parameters.forEach(p -> super.register(p));
  }

  protected NumberFunction(final String name, final boolean includeParenthesis, final Expression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    if (SUtil.isEmpty(name)) {
      throw new InvalidFunctionException("The function name cannot be empty");
    }
    this.name = name;
    this.qualifier = null;
    this.includeParenthesis = includeParenthesis;
    this.parameters = Arrays.asList(parameters);
    this.parameters.forEach(p -> super.register(p));
  }

  protected NumberFunction(final String name, final String qualifier, final boolean includeParenthesis,
      final Expression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    if (SUtil.isEmpty(name)) {
      throw new InvalidFunctionException("The function name cannot be empty");
    }
    this.name = name;
    this.qualifier = qualifier;
    this.includeParenthesis = includeParenthesis;
    this.parameters = Arrays.asList(parameters);
    this.parameters.forEach(p -> super.register(p));
  }

  @Override
  public void renderTo(final QueryWriter w) {
    Renderer.renderGenericFunctionTo(this.name, this.qualifier, this.includeParenthesis, this.parameters, w);
  }

}
