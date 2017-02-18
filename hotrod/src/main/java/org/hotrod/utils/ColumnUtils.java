package org.hotrod.utils;

public class ColumnUtils {

  public static long getMaxValue(final int digits) {
    if (digits < 1) {
      return 0;
    }
    if (digits > 18) {
      return Long.MAX_VALUE;
    }
    long value = 1;
    for (int i = 0; i < digits; i++) {
      value = value * 10;
    }
    return value - 1;
  }

  public static long getMinValue(final int digits) {
    if (digits > 18) {
      return Long.MIN_VALUE;
    }
    return -getMaxValue(digits);
  }

}
