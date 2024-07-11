package org.hotrod.runtime.typesolver;

public class THelper {

  public static String render(final TypeHandler typeHandler) {
    return typeHandler == null ? "null" : typeHandler.render();
  }

}
