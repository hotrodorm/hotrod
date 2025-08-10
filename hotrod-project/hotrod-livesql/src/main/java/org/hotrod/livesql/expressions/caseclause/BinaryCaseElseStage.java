package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.binary.BinarySyntaxExpression;

public class BinaryCaseElseStage {

  private BinaryCaseClause clause;

  public BinaryCaseElseStage(final BinaryCaseClause clause) {
    this.clause = clause;
  }

  public BinarySyntaxExpression end() {
    return this.clause;
  }

}
