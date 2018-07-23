package org.hotrod.utils.identifiers;

import org.hotrod.config.ColumnTag;
import org.hotrod.database.PropertyType;

public class ColumnIdentifier extends DbIdentifier {

  private static final long serialVersionUID = 1L;

  private boolean isBooleanType;

  public ColumnIdentifier(final String name, final PropertyType type, final ColumnTag columnTag) {
    super(name, columnTag == null ? null : columnTag.getJavaName());
    this.isBooleanType = type.isBooleanType();
  }

  public ColumnIdentifier(final String name, final String typeName) {
    super(name, null);
    this.isBooleanType = "boolean".equals(typeName);
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
    // return (this.isBooleanType ? "is" : "get")
    // + (startsWithLowerUpperCase() ? this.getJavaMemberIdentifier() :
    // this.getJavaClassIdentifier());
    return (this.isBooleanType ? "is" : "get") + this.getJavaClassIdentifier();
  }

  public String getSetter() {
    // return "set" + (startsWithLowerUpperCase() ?
    // this.getJavaMemberIdentifier() : this.getJavaClassIdentifier());
    return "set" + this.getJavaClassIdentifier();
  }

}
