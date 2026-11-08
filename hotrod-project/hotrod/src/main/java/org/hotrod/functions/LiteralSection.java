package org.hotrod.functions;

public class LiteralSection implements Section {

  private String literal;

  public LiteralSection(String literal) {
    this.literal = literal == null ? "" : literal;
  }

  public String getLiteral() {
    return literal;
  }

}
