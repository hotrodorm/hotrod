package org.hotrod.runtime.livesql.expressions.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.exceptions.InvalidFunctionException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrodorm.hotrod.utils.SUtil;
import org.hotrodorm.hotrod.utils.Separator;

public abstract class ObjectFunction extends ObjectExpression {

  private String name;
  private List<Expression<?>> parameters = new ArrayList<Expression<?>>();

  protected ObjectFunction(final String name, final Expression<?>... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    if (SUtil.isEmpty(name)) {
      throw new InvalidFunctionException("The function name cannot be empty");
    }
    this.name = name;
    this.parameters = Arrays.asList(parameters);
    this.parameters.forEach(p -> super.register(p));
  }

  protected ObjectFunction(final String name, final List<Expression<?>> parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    if (SUtil.isEmpty(name)) {
      throw new InvalidFunctionException("The function name cannot be empty");
    }
    this.name = name;
    this.parameters = parameters;
    this.parameters.forEach(p -> super.register(p));
  }

  protected ObjectFunction(final String name, final Stream<Expression<?>> parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    if (SUtil.isEmpty(name)) {
      throw new InvalidFunctionException("The function name cannot be empty");
    }
    this.name = name;
    this.parameters = parameters.collect(Collectors.toList());
    this.parameters.forEach(p -> super.register(p));
  }

  @Override
  public final void renderTo(final QueryWriter w) {
    w.write(this.name);
    w.write("(");
    Separator sep = new Separator();
    for (Expression<?> p : this.parameters) {
      w.write(sep.render());
      p.renderTo(w);
    }
    w.write(")");
  }

  protected Expression<?>[] concat(final Expression<?> a, final Expression<?>... b) {
    return Stream.concat(Stream.of(a), Stream.of(a)).toArray(Expression<?>[]::new);
  }

}
