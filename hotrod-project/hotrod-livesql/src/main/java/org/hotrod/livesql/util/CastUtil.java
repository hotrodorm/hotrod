package org.hotrod.livesql.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hotrod.livesql.exceptions.LiveSQLException;

public class CastUtil {

  private static final String FORMS = "WORD, WORD WORD..., WORD(NUMBER), WORD(NUMBER,NUMBER), WORD[], WORD[] WORD";
  private static Pattern TYPE_PATTERN = Pattern.compile(
      "^" + "[a-zA-Z][a-zA-Z0-9\\ ]*" + "(\\([0-9]+(,\\ *[0-9]+)?\\))?" + "(\\[\\])?" + "[a-zA-Z0-9\\ ]*" + "$");

  // PostgreSQL:
  // INTEGER[]

  // MySQL:
  // AT TIME ZONE '+00:00' AS DATETIME(2)
  // CHAR CHARACTER SET latin1
  // CHAR(50) CHARACTER SET latin1

  // MariaDB:
  // CHAR CHARACTER SET utf8

  public static void validateCastType(String type) {
    if (type == null) {
      throw new LiveSQLException(
          "Invalid type used in CAST: '" + type + "'; can only use the forms: " + FORMS + ", etc.");
    } else {
      Matcher m = TYPE_PATTERN.matcher(type);
      boolean valid = m.find();
      if (!valid) {
        throw new LiveSQLException(
            "Invalid type used in CAST: '" + type + "'; can only use the forms: " + FORMS + ", etc.");
      }
    }
  }

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
