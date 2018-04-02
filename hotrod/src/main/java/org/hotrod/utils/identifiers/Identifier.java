package org.hotrod.utils.identifiers;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hotrod.runtime.util.ListWriter;

public abstract class Identifier implements Serializable {

  private static final long serialVersionUID = 1L;

  private static Logger log = Logger.getLogger(Identifier.class);

  protected boolean hasSQLName = true;
  protected String SQLName = null;
  protected String javaClassName = null;
  protected String javaMemberName = null;
  protected String javaConstantName = null;

  protected String[] javaChunks;
  protected String[] dbChunks;

  private static final char JAVA_SEPARATOR = '_';

  public String getSQLIdentifier() {
    if (this.SQLName != null) {
      return this.SQLName;
    }
    StringBuffer id = new StringBuffer();
    for (int i = 0; i < this.dbChunks.length; i++) {
      if (i > 0) {
        id.append(JAVA_SEPARATOR);
      }
      id.append(this.dbChunks[i]);
    }
    return id.toString();
  }

  public String getLowerCaseDbIdentifier() {
    if (this.SQLName != null) {
      return this.SQLName.toLowerCase();
    }
    StringBuffer id = new StringBuffer();
    for (int i = 0; i < this.dbChunks.length; i++) {
      if (i > 0) {
        id.append(JAVA_SEPARATOR);
      }
      id.append(this.javaChunks[i]);
    }
    return id.toString();
  }

  public boolean wasJavaNameSpecified() {
    return this.javaClassName != null;
  }

  public String getJavaClassIdentifier() {
    if (this.javaClassName != null) {
      return this.javaClassName;
    }
    StringBuffer id = new StringBuffer();
    for (int i = 0; i < this.javaChunks.length; i++) {
      id.append(capitalizeFirst(this.javaChunks[i]));
    }
    return id.toString();
  }

  public String getJavaMemberIdentifier() {
    if (this.javaMemberName != null) {
      return this.javaMemberName;
    }
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

  public String getMapperIdentifier() {
    return this.getJavaClassIdentifier();
  }

  public String getMapperFileIdentifier() {
    ListWriter lw = new ListWriter("-");
    for (String c : this.javaChunks) {
      lw.add(c.toLowerCase());
    }
    return lw.toString();
  }

  public String getJavaConstantIdentifier() {
    StringBuffer id = new StringBuffer();
    for (int i = 0; i < this.javaChunks.length; i++) {
      if (i != 0) {
        id.append(JAVA_SEPARATOR);
      }
      id.append(this.javaChunks[i].toUpperCase());
    }
    return id.toString();
  }

  public String getLowerCaseIdentifier() {
    StringBuffer id = new StringBuffer();
    for (int i = 0; i < this.javaChunks.length; i++) {
      id.append(this.javaChunks[i]);
    }
    return id.toString();
  }

  protected String capitalizeFirst(final String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  protected String uncapitalizeFirst(final String name) {
    return name.substring(0, 1).toLowerCase() + name.substring(1);
  }

  // private boolean startsWithTwoUpperCase() {
  // if (isUpperCase(this.javaChunks[0].charAt(0))) {
  // if (this.javaChunks[0].length() >= 2) {
  // if (isUpperCase(this.javaChunks[0].charAt(1))) {
  // return true;
  // }
  // } else {
  // if (isUpperCase(this.javaChunks[1].charAt(0))) {
  // return true;
  // }
  // }
  // }
  // return false;
  // }

  protected boolean startsWithLowerUpperCase() {
    log.debug("(1) -> " + isLowerCase(this.javaChunks[0].charAt(0)));
    if (isLowerCase(this.javaChunks[0].charAt(0))) {
      log.debug("(2)");
      if (this.javaChunks[0].length() >= 2) {
        log.debug("(3)");
        if (isUpperCase(this.javaChunks[0].charAt(1))) {
          log.debug("(4)");
          return true;
        }
      } else {
        log.debug("(5)");
        return true;
      }
      log.debug("(6)");
    }
    log.debug("(7)");
    return false;
  }

  private boolean isLowerCase(final char c) {
    return c >= 'a' && c <= 'z';
  }

  private boolean isUpperCase(final char c) {
    return c >= 'A' && c <= 'Z';
  }

  public boolean hasSQLName() {
    return hasSQLName;
  }

}
