package org.hotrod.runtime.livesql.util;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.binary.ByteArrayConstant;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeConstant;
import org.hotrod.runtime.livesql.expressions.numbers.NumberConstant;
import org.hotrod.runtime.livesql.expressions.object.ObjectConstant;
import org.hotrod.runtime.livesql.expressions.predicates.BooleanConstant;
import org.hotrod.runtime.livesql.expressions.strings.StringConstant;

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
