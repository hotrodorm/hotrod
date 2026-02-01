package org.hotrod.livesql.queries.typesolver;

public class TShield {

  public static String render(final TypeHandler<?, ?> typeHandler) {
    return typeHandler == null ? "N/A" : typeHandler.render();
  }

}
