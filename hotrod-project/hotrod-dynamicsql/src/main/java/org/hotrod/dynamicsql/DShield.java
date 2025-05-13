package org.hotrod.dynamicsql;

public class DShield {

  public static boolean hasParameter(Parameters params, String name) {
    return params.hasParameter(name);
  }

  public static Object getParameterValue(Parameters params, String name) {
    return params.getParameterValue(name);
  }

  public static void bind(Parameters params, String name, Object obj) throws DynamicExpressionException {
    params.bind(name, obj);
  }

  public static Object unbind(Parameters params, String name) {
    return params.unbind(name);
  }

}
