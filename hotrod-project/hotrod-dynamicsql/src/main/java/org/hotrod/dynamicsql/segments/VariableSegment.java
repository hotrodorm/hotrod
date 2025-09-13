package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.parameters.ParameterOccurrence;
import org.hotrod.dynamicsql.parameters.VariableOccurrence;

public class VariableSegment extends DynamicContentSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(VariableSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private TypeConverter<?, ?> converter;

  private DynamicExpression nameExpression;

  public VariableSegment(DynamicExpressionFactory factory, String name, TypeConverter<?, ?> converter) {
    this.factory = factory;
    this.name = name;
    this.converter = converter;
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    Object v = this.nameExpression.evaluate(context, Object.class);
    Integer index = ParameterOccurrence.getCounterAndIncrement(this, loopNestingLevel);
    VariableOccurrence is = new VariableOccurrence(this.name, index, v, this.converter);
    sc.consume("?");
    sc.consume(is);
    return true;
  }

}
