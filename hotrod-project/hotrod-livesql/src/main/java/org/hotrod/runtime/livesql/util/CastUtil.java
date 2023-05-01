package org.hotrod.runtime.livesql.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CastUtil {

  public static Byte toByte(final Number n) {
    return n == null ? null : n.byteValue();
  }

  public static Short toShort(final Number n) {
    return n == null ? null : n.shortValue();
  }

  public static Integer toInteger(final Number n) {
    return n == null ? null : n.intValue();
  }

  public static Long toLong(final Number n) {
    return n == null ? null : n.longValue();
  }

  public static Float toFloat(final Number n) {
    return n == null ? null : n.floatValue();
  }

  public static Double toDouble(final Number n) {
    return n == null ? null : n.doubleValue();
  }

  public static BigInteger toBigInteger(final Number n) {
    if (n == null)
      return null;
    try {
      return (BigInteger) n;
    } catch (ClassCastException e) {
      try {
        return ((BigDecimal) n).toBigInteger();
      } catch (ClassCastException e1) {
        return BigInteger.valueOf(n.longValue());
      }
    }
  }

  public static BigDecimal toBigDecimal(final Number n) {
    if (n == null)
      return null;
    try {
      return (BigDecimal) n;
    } catch (ClassCastException e) {
      try {
        return new BigDecimal((BigInteger) n);
      } catch (ClassCastException e1) {
        if (n instanceof Double || n instanceof Float) {
          return BigDecimal.valueOf(n.doubleValue());
        } else {
          return BigDecimal.valueOf(n.longValue());
        }
      }
    }
  }

}
