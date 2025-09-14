package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.parameters.ParameterNotNullableOccurrence;

public class ParameterNotNullableSegment extends DynamicContentSegment {

  private static final Logger log = Logger.getLogger(ParameterNotNullableSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private TypeConverter<?, ?> converter;

  private DynamicExpression nameExpression;

  public ParameterNotNullableSegment(DynamicExpressionFactory factory, String name, TypeConverter<?, ?> converter) {
    log.fine("init");
    this.factory = factory;
    this.name = name;
    this.converter = converter;
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters params, int loopNestingLevel)
      throws DynamicExpressionException {
    Object v = this.nameExpression.evaluate(params, Object.class);
    Integer index = params.getIndexAndIncrement(this, loopNestingLevel);
    ParameterNotNullableOccurrence is = new ParameterNotNullableOccurrence(this.name, index, v, this.converter);
    sc.consume("?");
    sc.consume(is);
    return true;
  }

}
