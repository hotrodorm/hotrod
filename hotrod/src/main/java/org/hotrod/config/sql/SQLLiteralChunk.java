package org.hotrod.config.sql;

import org.hotrod.config.dynamicsql.SQLChunk;
import org.hotrod.generator.ParameterRenderer;

@Deprecated
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


}
