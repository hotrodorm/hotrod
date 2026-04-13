package org.hotrod.identifiers;

import java.util.ArrayList;
import java.util.List;

public class TypedSQLName {

  private static final List<SQLQuoteDelimiters> DELIMITERS;
  static {
    DELIMITERS = new ArrayList<SQLQuoteDelimiters>();
    DELIMITERS.add(new SQLQuoteDelimiters("'", "'"));
    DELIMITERS.add(new SQLQuoteDelimiters("\"", "\""));
    DELIMITERS.add(new SQLQuoteDelimiters("`", "`"));
    DELIMITERS.add(new SQLQuoteDelimiters("[", "]"));
  }

  private String name;
  private boolean quoted;

  public TypedSQLName(final String typedSQLName) {
    if (typedSQLName == null) {
      throw new IllegalArgumentException("typedName cannot be null");
    }
    for (SQLQuoteDelimiters d : DELIMITERS) {
      if (typedSQLName.startsWith(d.getOpening()) //
          && typedSQLName.endsWith(d.getClosing()) //
          && typedSQLName.length() > (d.getOpening().length() + d.getClosing().length()) //
      ) {
        this.name = typedSQLName.substring(d.getOpening().length(), typedSQLName.length() - d.getClosing().length());
        this.quoted = true;
        return;
      }
    }
    this.name = typedSQLName;
    this.quoted = false;
  }

  public static TypedSQLName of(String typedSQLName) {
    return new TypedSQLName(typedSQLName);
  }

  public String getName() {
    return name;
  }

  public boolean isQuoted() {
    return quoted;
  }

  public String toString() {
    return (this.quoted ? "@" : "") + this.name + (this.quoted ? "@" : "");
  }

  // Helpers

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + (quoted ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TypedSQLName other = (TypedSQLName) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (quoted != other.quoted)
      return false;
    return true;
  }

  private static class SQLQuoteDelimiters {

    private String opening;
    private String closing;

    private SQLQuoteDelimiters(String opening, String closing) {
      super();
      this.opening = opening;
      this.closing = closing;
    }

    public String getOpening() {
      return opening;
    }

    public String getClosing() {
      return closing;
    }

  }

}
