package org.hotrod.config;

import org.hotrod.generator.ParameterRenderer;

public class SQLLiteralChunk implements SQLChunk {

  private String literal;

  public SQLLiteralChunk(final String literal) {
    this.literal = literal;
  }

  public String getLiteral() {
    return literal;
  }

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return this.literal;
  }

  @Override
  public String renderAugmentedSQL() {
    return this.literal;
  }

}
