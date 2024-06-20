package org.hotrod.runtime.livesql.metadata;

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

  // Getters

  public String getName() {
    return name;
  }

  public boolean isQuoted() {
    return quoted;
  }

  public String toString() {
    return this.name;
  }

}
