package org.hotrod.utils.identifiers;

public class DataSetIdentifier extends DbIdentifier {

  private static final long serialVersionUID = 1L;

  public DataSetIdentifier(final String name) {
    super(name);
  }

  public DataSetIdentifier(final String name, final String javaName) {
    super(name, javaName);
  }

  public DataSetIdentifier(final String name, final String javaName, final boolean hasSQLName) {
    super(name, javaName);
    this.hasSQLName = hasSQLName;
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

}
