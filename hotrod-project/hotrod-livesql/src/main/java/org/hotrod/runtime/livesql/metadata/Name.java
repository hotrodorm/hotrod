package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public class Name {

  private String name;
  private boolean quoted;

  private Name(final String name, final boolean quoted) {
    this.name = name;
    this.quoted = quoted;
  }

  // Constructors

  public static Name of(final String name, final boolean quoted) {
    return new Name(name, quoted);
  }

  public static Name parse(final String natural) {
    if (natural == null || natural.trim().length() == 0) {
      return null;
    }
    if (natural.startsWith("'") && natural.endsWith("'")) {
      if (natural.length() == 2) {
        return null;
      }
      return new Name(natural.substring(1, natural.length() - 1), true);
    } else {
      return new Name(natural, false);
    }
  }

  public void renderTo(final QueryWriter w) {
    if (this.quoted) {
      w.write(w.getSQLDialect().quoteIdentifier(this.name));
    } else {
      w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.name)));
    }
  }

  // Getters

  public String getName() {
    return this.name;
  }

  public boolean isQuoted() {
    return this.quoted;
  }

  public String toString() {
    return this.name;
  }

}
