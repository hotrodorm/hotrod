package org.hotrod.livesql.util;

import java.time.temporal.Temporal;
import java.util.Date;

import org.hotrod.livesql.expressions.binary.BinaryConstant;
import org.hotrod.livesql.expressions.bool.BooleanConstant;
import org.hotrod.livesql.expressions.character.CharConstant;
import org.hotrod.livesql.expressions.datetime.DateTimeConstant;
import org.hotrod.livesql.expressions.numeric.NumericConstant;
import org.hotrod.livesql.expressions.object.ObjectConstant;

public class BoxUtil {

  public static NumericConstant box(final Number value) {
    return new NumericConstant(value);
  }

  public static CharConstant box(final String value) {
    return new CharConstant(value);
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

  public static BinaryConstant box(final byte[] value) {
    return new BinaryConstant(value);
  }

  public static ObjectConstant box(final Object value) {
    return new ObjectConstant(value);
  }

}
