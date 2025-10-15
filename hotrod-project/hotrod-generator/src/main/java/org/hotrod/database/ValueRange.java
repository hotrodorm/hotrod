package org.hotrod.database;

import java.util.logging.Logger;

import org.hotrod.utils.ColumnUtils;

public class ValueRange {

  private static final Logger log = Logger.getLogger(ValueRange.class.getName());

  public static final ValueRange BYTE_RANGE = new ValueRange(0, Byte.MIN_VALUE, Byte.MAX_VALUE);
  public static final ValueRange SHORT_RANGE = new ValueRange(0, Short.MIN_VALUE, Short.MAX_VALUE);
  public static final ValueRange INTEGER_RANGE = new ValueRange(0, Integer.MIN_VALUE, Integer.MAX_VALUE);
  public static final ValueRange LONG_RANGE = new ValueRange(0, Long.MIN_VALUE, Long.MAX_VALUE);

  public static final ValueRange UNSIGNED_BYTE_RANGE = new ValueRange(0, 0, 2L * Byte.MAX_VALUE + 1);
  public static final ValueRange UNSIGNED_SHORT_RANGE = new ValueRange(0, 0, 2L * Short.MAX_VALUE + 1);
  public static final ValueRange UNSIGNED_INTEGER_RANGE = new ValueRange(0, 0, 2L * Integer.MAX_VALUE + 1);

  private long initialValue;
  private long minValue;
  private long maxValue;

  public ValueRange(long initialValue, long minValue, long maxValue) {
    log.fine("init");
    this.initialValue = initialValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  public long getInitialValue() {
    return initialValue;
  }

  public long getMinValue() {
    return minValue;
  }

  public long getMaxValue() {
    return maxValue;
  }

  public static ValueRange getSignedRange(final int size) {
    return new ValueRange(0, ColumnUtils.getMinValue(size), ColumnUtils.getMaxValue(size));
  }

  public static ValueRange getUnsignedRange(final int size) {
    return new ValueRange(0, 0, ColumnUtils.getMaxValue(size));
  }

  public String toString() {
    return "[min=" + this.minValue + ", max=" + this.maxValue + ", initial=" + this.initialValue + "]";
  }

}
