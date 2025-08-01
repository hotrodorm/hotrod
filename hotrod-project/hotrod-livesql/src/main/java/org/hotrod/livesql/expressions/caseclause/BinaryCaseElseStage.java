package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.binary.BinaryExpression;

public class BinaryCaseElseStage {

  private BinaryCaseClause clause;

  public BinaryCaseElseStage(final BinaryCaseClause clause) {
    this.clause = clause;
  }

  public BinaryExpression end() {
    return this.clause;
  }

}
