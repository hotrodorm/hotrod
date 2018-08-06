package org.hotrod.utils.identifiers2;

import java.util.ArrayList;
import java.util.List;

public class SQLName {

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

  public SQLName(final String typedName) {
    if (typedName == null) {
      throw new IllegalArgumentException("typedName cannot be null");
    }
    for (SQLQuoteDelimiters d : DELIMITERS) {
      if (typedName.startsWith(d.getOpening()) //
          && typedName.endsWith(d.getClosing()) //
          && typedName.length() > (d.getOpening().length() + d.getClosing().length()) //
      ) {
        this.name = typedName.substring(d.getOpening().length(), typedName.length() - d.getClosing().length());
        this.quoted = true;
        return;
      }
    }
    this.name = typedName;
    this.quoted = false;
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
