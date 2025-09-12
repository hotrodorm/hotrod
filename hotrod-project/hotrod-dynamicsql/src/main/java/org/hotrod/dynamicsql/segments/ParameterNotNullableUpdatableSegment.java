package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.parameters.ParameterInstance;
import org.hotrod.dynamicsql.parameters.ParameterNotNullableUpdatableInstance;

public class ParameterNotNullableUpdatableSegment extends DynamicContentSegment {

  private static final Logger log = Logger.getLogger(ParameterNotNullableUpdatableSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private TypeConverter<?, ?> converter;

  private DynamicExpression nameExpression;

  public ParameterNotNullableUpdatableSegment(DynamicExpressionFactory factory, String name,
      TypeConverter<?, ?> converter) {
    this.factory = factory;
    this.name = name;
    this.converter = converter;
    log.finer("<<parameter>> this.name=" + this.name);
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    Object v = this.nameExpression.evaluate(context, Object.class);
    Integer index = ParameterInstance.getCounterAndIncrement(this, loopNestingLevel);
    ParameterNotNullableUpdatableInstance is = new ParameterNotNullableUpdatableInstance(this.name, index, v,
        this.converter);
    sc.consume("?");
    sc.consume(is);
    return true;
  }

}
