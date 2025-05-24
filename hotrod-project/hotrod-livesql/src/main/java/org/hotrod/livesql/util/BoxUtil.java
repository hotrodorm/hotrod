package org.hotrod.livesql.util;

import java.time.temporal.Temporal;
import java.util.Date;

import org.hotrod.livesql.expressions.binary.ByteArrayConstant;
import org.hotrod.livesql.expressions.datetime.DateTimeConstant;
import org.hotrod.livesql.expressions.numbers.NumberConstant;
import org.hotrod.livesql.expressions.object.ObjectConstant;
import org.hotrod.livesql.expressions.predicates.BooleanConstant;
import org.hotrod.livesql.expressions.strings.StringConstant;

public class BoxUtil {

  public static NumberConstant box(final Number value) {
    return new NumberConstant(value);
  }

  public static StringConstant box(final String value) {
    return new StringConstant(value);
  }

  public static DateTimeConstant box(final Date value) {
    return new DateTimeConstant(value);
  }

  public static DateTimeConstant box(final Temporal value) {
    return new DateTimeConstant(value);
  }

  public static BooleanConstant box(final Boolean value) {
    return new BooleanConstant(value);
  }

  public static ByteArrayConstant box(final byte[] value) {
    return new ByteArrayConstant(value);
  }

  public static ObjectConstant box(final Object value) {
    return new ObjectConstant(value);
  }

}
