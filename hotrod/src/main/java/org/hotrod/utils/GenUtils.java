package org.hotrod.utils;

import org.hotrod.ant.Constants;
import org.hotrod.exceptions.ControlledException;

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

  public static String convertPropertyType(final String sourceClass, final String targetClass, final String var)
      throws ControlledException {
    if (sourceClass == null) {
      if (targetClass == null) {
        return var;
      } else {
        throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
            + " cannot convert from '" + sourceClass + "' to '" + targetClass + "'.");
      }
    } else {
      if (sourceClass.equals(targetClass)) {
        return var;
      } else {
        if (BIG_DECIMAL.equals(sourceClass)) {
          return bigDecimalTo(targetClass, var);
        }
        if (BIG_INTEGER.equals(sourceClass)) {
          return bigIntegerTo(targetClass, var);
        }
        if (LONG.equals(sourceClass) || S_LONG.equals(sourceClass)) {
          return longTo(targetClass, var);
        }
        if (INTEGER.equals(sourceClass) || S_INTEGER.equals(sourceClass)) {
          return integerTo(targetClass, var);
        }
        if (SHORT.equals(sourceClass) || S_SHORT.equals(sourceClass)) {
          return shortTo(targetClass, var);
        }
        if (BYTE.equals(sourceClass) || S_BYTE.equals(sourceClass)) {
          return byteTo(targetClass, var);
        }
        if (DOUBLE.equals(sourceClass) || S_DOUBLE.equals(sourceClass)) {
          return doubleTo(targetClass, var);
        }
        if (FLOAT.equals(sourceClass) || S_FLOAT.equals(sourceClass)) {
          return floatTo(targetClass, var);
        }
        throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
            + " cannot convert from '" + sourceClass + "' to '" + targetClass + "'.");
      }
    }
  }

  private static String bigDecimalTo(final String targetClass, final String var) throws ControlledException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return var;
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : " + var + ".toBigInteger()";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Long(" + var + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Integer(" + var + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Short(" + var + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Byte(" + var + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Double(" + var + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Float(" + var + ".floatValue())";
    }
    throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'BigDecimal' to '" + targetClass + "'.");
  }

  private static String bigIntegerTo(final String targetClass, final String var) throws ControlledException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigDecimal(" + var + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return var;
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Long(" + var + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Integer(" + var + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Short(" + var + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Byte(" + var + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Double(" + var + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Float(" + var + ".floatValue())";
    }
    throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'BigDecimal' to '" + targetClass + "'.");
  }

  private static String longTo(final String targetClass, final String var) throws ControlledException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigDecimal(" + var + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigInteger(" + var + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return var;
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Integer(" + var + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Short(" + var + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Byte(" + var + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Double(" + var + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Float(" + var + ".floatValue())";
    }
    throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  private static String integerTo(final String targetClass, final String var) throws ControlledException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigDecimal(" + var + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigInteger(" + var + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Long(" + var + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return var;
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Short(" + var + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Byte(" + var + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Double(" + var + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Float(" + var + ".floatValue())";
    }
    throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  private static String shortTo(final String targetClass, final String var) throws ControlledException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigDecimal(" + var + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigInteger(" + var + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Long(" + var + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Integer(" + var + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return var;
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Byte(" + var + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Double(" + var + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Float(" + var + ".floatValue())";
    }
    throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  private static String byteTo(final String targetClass, final String var) throws ControlledException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigDecimal(" + var + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigInteger(" + var + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Long(" + var + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Integer(" + var + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Short(" + var + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return var;
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Double(" + var + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Float(" + var + ".floatValue())";
    }
    throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

  private static String doubleTo(final String targetClass, final String var) throws ControlledException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigDecimal(" + var + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigInteger(" + var + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Long(" + var + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Integer(" + var + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Short(" + var + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Byte(" + var + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return var;
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Float(" + var + ".floatValue())";
    }
    throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
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

  private static String floatTo(final String targetClass, final String var) throws ControlledException {
    if (BIG_DECIMAL.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigDecimal(" + var + ")";
    }
    if (BIG_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new java.math.BigInteger(" + var + ".toString())";
    }
    if (LONG.equals(targetClass) || S_LONG.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Long(" + var + ".longValue())";
    }
    if (INTEGER.equals(targetClass) || S_INTEGER.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Integer(" + var + ".intValue())";
    }
    if (SHORT.equals(targetClass) || S_SHORT.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Short(" + var + ".shortValue())";
    }
    if (BYTE.equals(targetClass) || S_BYTE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Byte(" + var + ".byteValue())";
    }
    if (DOUBLE.equals(targetClass) || S_DOUBLE.equals(targetClass)) {
      return "(" + var + " == null) ? null : new Double(" + var + ".doubleValue())";
    }
    if (FLOAT.equals(targetClass) || S_FLOAT.equals(targetClass)) {
      return var;
    }
    throw new ControlledException("Unrecognized FK data conversion: " + Constants.TOOL_NAME
        + " cannot convert from 'Long' to '" + targetClass + "'.");
  }

}
