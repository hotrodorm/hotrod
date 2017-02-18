package org.hotrod.utils.identifiers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.database.PropertyType;

public class JavaIdentifier extends Identifier {

  private static Logger log = Logger.getLogger(JavaIdentifier.class);

  private boolean isBooleanType;

  public JavaIdentifier(final String name, final PropertyType type) {
    log.debug("init");
    initialize(name);
    this.isBooleanType = type.isBooleanType();
  }

  public JavaIdentifier(final String name, final String type) {
    log.debug("init");
    initialize(name);
    this.isBooleanType = PropertyType.isBooleanType(type);
  }

  private void initialize(final String name) {
    List<String> chunks = new ArrayList<String>();
    int pos = 0;
    int start = pos;
    while (pos < name.length()) {
      if (Character.isUpperCase(name.charAt(pos))) {
        if (pos > start) {
          chunks.add(name.substring(start, pos).toLowerCase());
          start = pos;
        }
      }
      pos++;
    }
    if (start < name.length()) {
      chunks.add(name.substring(start).toLowerCase());
    }

    this.javaChunks = chunks.toArray(new String[chunks.size()]);
    this.dbChunks = this.javaChunks;
  }

  public String getGetter() {
    return (this.isBooleanType ? "is" : "get")
        + (startsWithLowerUpperCase() ? this.getJavaMemberIdentifier() : this.getJavaClassIdentifier());
  }

}
