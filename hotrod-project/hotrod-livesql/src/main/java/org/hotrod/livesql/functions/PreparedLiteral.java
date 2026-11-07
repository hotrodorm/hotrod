package org.hotrod.livesql.functions;

import org.hotrod.livesql.queries.QueryWriter;

public class PreparedLiteral extends PreparedSegment {

  private String literal;

  private PreparedLiteral(String literal) {
    this.literal = literal;
  }

  static PreparedLiteral of(String literal) {
    return new PreparedLiteral(literal);
  }

  @Override
  public void renderTo(QueryWriter w) {
    w.write(this.literal);
  }

}