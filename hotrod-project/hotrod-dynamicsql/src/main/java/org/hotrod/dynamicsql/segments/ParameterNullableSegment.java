package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.parameters.ParameterNullableOccurrence;

public class ParameterNullableSegment extends DynamicContentSegment {

  private static final Logger log = Logger.getLogger(ParameterNullableSegment.class.getName());

  private DynamicExpressionFactory factory;
  private String name;
  private int sqlType;
  private TypeConverter<?, ?> converter;

  private DynamicExpression nameExpression;

  public ParameterNullableSegment(DynamicExpressionFactory factory, String name, int sqlType,
      TypeConverter<?, ?> converter) {
    log.fine("init");
    this.factory = factory;
    this.name = name;
    this.sqlType = sqlType;
    this.converter = converter;
    this.nameExpression = this.factory.expression(this.name);
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters params, int loopNestingLevel)
      throws DynamicExpressionException {
    Object v = this.nameExpression.evaluate(params, Object.class);
    Integer index = params.getIndexAndIncrement(this, loopNestingLevel);
    ParameterNullableOccurrence is = new ParameterNullableOccurrence(this.sqlType, this.name, index, v, this.converter);
    sc.consume("?");
    sc.consume(is);
    return true;
  }

}
