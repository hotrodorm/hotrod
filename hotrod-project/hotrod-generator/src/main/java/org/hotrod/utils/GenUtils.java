package org.hotrod.utils;

import org.hotrod.config.Constants;
import org.hotrod.exceptions.ErrorMessageException;

public class GenUtils {

  private static final String BIG_DECIMAL = "java.math.BigDecimal";
  private static final String BIG_INTEGER = "java.math.BigInteger";

  private static final String LONG = "java.lang.Long";
  private static final String S_LONG = "Long";
  private static final String INTEGER = "java.lang.Integer";
  private static final String S_INTEGER = "Integer";
  private static final String SHORT = "java.lang.Short";
  private static final String S_SHORT = "Short";
  private static final String BYTE = "java.lang.Byte";
  private static final String S_BYTE = "Byte";

  private static final String DOUBLE = "java.lang.Double";
  private static final String S_DOUBLE = "Double";
  private static final String FLOAT = "java.lang.Float";
  private static final String S_FLOAT = "Float";

  private GenUtils() {
  }

  public static String convertPropertyType(final String sourceClass, final String targetClass, final String v)
      throws ErrorMessageException {
    if (sourceClass == null) {
      if (targetClass == null) {
        return v;
      } else {
        throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
            + " cannot convert from '" + sourceClass + "' to '" + targetClass + "'.");
      }
    } else {
      if (sourceClass.equals(targetClass)) {
        return v;
      } else {
        if (BIG_DECIMAL.equals(sourceClass)) {
          return bigDecimalTo(targetClass, v);
        }
        if (BIG_INTEGER.equals(sourceClass)) {
          return bigIntegerTo(targetClass, v);
        }
        if (LONG.equals(sourceClass) || S_LONG.equals(sourceClass)) {
          return longTo(targetClass, v);
        }
        if (INTEGER.equals(sourceClass) || S_INTEGER.equals(sourceClass)) {
          return integerTo(targetClass, v);
        }
        if (SHORT.equals(sourceClass) || S_SHORT.equals(sourceClass)) {
          return shortTo(targetClass, v);
        }
        if (BYTE.equals(sourceClass) || S_BYTE.equals(sourceClass)) {
          return byteTo(targetClass, v);
        }
        if (DOUBLE.equals(sourceClass) || S_DOUBLE.equals(sourceClass)) {
          return doubleTo(targetClass, v);
        }
        if (FLOAT.equals(sourceClass) || S_FLOAT.equals(sourceClass)) {
          return floatTo(targetClass, v);
        }
        throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
            + " cannot convert from '" + sourceClass + "' to '" + targetClass + "'.");
      }
    }
  }

  private static String bigDecimalTo(final String targetClass, final String v) throws ErrorMessageException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return v;
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : " + v + ".toBigInteger()";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + v + " == null) ? null : Long.valueOf(" + v + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : Integer.valueOf(" + v + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Short.valueOf(" + v + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Byte.valueOf(" + v + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Double.valueOf(" + v + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Float.valueOf(" + v + ".floatValue())";
    }
    throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'BigDecimal' to '" + targetClass + "'.");
  }

  private static String bigIntegerTo(final String targetClass, final String v) throws ErrorMessageException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigDecimal(" + v + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return v;
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + v + " == null) ? null : Long.valueOf(" + v + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : Integer.valueOf(" + v + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Short.valueOf(" + v + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Byte.valueOf(" + v + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Double.valueOf(" + v + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Float.valueOf(" + v + ".floatValue())";
    }
    throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'BigDecimal' to '" + targetClass + "'.");
  }

  private static String longTo(final String targetClass, final String v) throws ErrorMessageException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigDecimal(" + v + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigInteger(" + v + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return v;
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : Integer.valueOf(" + v + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Short.valueOf(" + v + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Byte.valueOf(" + v + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Double.valueOf(" + v + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Float.valueOf(" + v + ".floatValue())";
    }
    throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  private static String integerTo(final String targetClass, final String v) throws ErrorMessageException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigDecimal(" + v + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigInteger(" + v + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + v + " == null) ? null : Long.valueOf(" + v + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return v;
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Short.valueOf(" + v + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Byte.valueOf(" + v + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Double.valueOf(" + v + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Float.valueOf(" + v + ".floatValue())";
    }
    throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  private static String shortTo(final String targetClass, final String v) throws ErrorMessageException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigDecimal(" + v + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigInteger(" + v + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + v + " == null) ? null : Long.valueOf(" + v + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : Integer.valueOf(" + v + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return v;
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Byte.valueOf(" + v + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Double.valueOf(" + v + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Float.valueOf(" + v + ".floatValue())";
    }
    throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  private static String byteTo(final String targetClass, final String v) throws ErrorMessageException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigDecimal(" + v + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigInteger(" + v + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + v + " == null) ? null : Long.valueOf(" + v + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : Integer.valueOf(" + v + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Short.valueOf(" + v + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return v;
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Double.valueOf(" + v + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Float.valueOf(" + v + ".floatValue())";
    }
    throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  private static String doubleTo(final String targetClass, final String v) throws ErrorMessageException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigDecimal(" + v + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigInteger(" + v + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + v + " == null) ? null : Long.valueOf(" + v + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : Integer.valueOf(" + v + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Short.valueOf(" + v + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Byte.valueOf(" + v + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return v;
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Float.valueOf(" + v + ".floatValue())";
    }
    throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  // private void test() {
  // Float i = 123.45f;
  //
  // BigDecimal bd = new BigDecimal(i);
  // BigInteger bi = new BigInteger(i.toString());
  // Long l = i.longValue();
  // Short s = i.shortValue();
  // Byte b = i.byteValue();
  // Double d = i.doubleValue();
  // Float f = i.floatValue();
  // }

  private static String floatTo(final String targetClass, final String v) throws ErrorMessageException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigDecimal(" + v + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : new java.math.BigInteger(" + v + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + v + " == null) ? null : Long.valueOf(" + v + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + v + " == null) ? null : Integer.valueOf(" + v + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + v + " == null) ? null : Short.valueOf(" + v + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Byte.valueOf(" + v + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + v + " == null) ? null : Double.valueOf(" + v + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return v;
    }
    throw new ErrorMessageException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

}
