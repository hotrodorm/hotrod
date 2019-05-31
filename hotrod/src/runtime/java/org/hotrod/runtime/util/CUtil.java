package org.hotrod.runtime.util;

public class CUtil {

  public static String renderObjectClass(final Object obj) {
    if (obj == null) {
      return null;
    }
    return renderClass(obj.getClass());
  }

  public static String renderClass(final Class<?> c) {
    if (c == null) {
      return null;
    }
    Class<?> componentType = c.getComponentType();
    // System.out.println("------> " + componentType);
    if (componentType == null) {
      return c.getName();
    } else {
      return CUtil.renderClass(componentType) + "[]";
    }
  }

}
