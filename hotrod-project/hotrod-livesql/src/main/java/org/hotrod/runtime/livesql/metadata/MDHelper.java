package org.hotrod.runtime.livesql.metadata;

public class MDHelper {

  public static void removeAlias(final TableOrView tov) {
    tov.removeAlias();
  }

  public static String renderUnescapedName(final TableOrView tov) {
    return tov.renderUnescapedName();
  }

}
