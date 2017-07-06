package org.hotrod.utils.identifiers;

import org.hotrod.config.tags.ColumnTag;
import org.hotrod.database.PropertyType;

public class ColumnIdentifier extends DbIdentifier {

  private PropertyType type;

  public ColumnIdentifier(final String name, final PropertyType type, final ColumnTag columnTag) {
    super(name, columnTag == null ? null : columnTag.getJavaName());
    this.type = type;
  }

  public String getJavaMemberIdentifier() {
    StringBuffer id = new StringBuffer();
    for (int i = 0; i < this.javaChunks.length; i++) {
      if (i > 0) {
        id.append(capitalizeFirst(this.javaChunks[i]));
      } else {
        id.append(this.javaChunks[i]);
      }
    }
    return id.toString();
  }

  public String getGetter() {
    return (this.type.isBooleanType() ? "is" : "get")
        + (startsWithLowerUpperCase() ? this.getJavaMemberIdentifier() : this.getJavaClassIdentifier());
  }

  public String getSetter() {
    return "set" + (startsWithLowerUpperCase() ? this.getJavaMemberIdentifier() : this.getJavaClassIdentifier());
  }

}
