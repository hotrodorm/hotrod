package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.binary.GeneralBinaryExpression;
import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
import org.hotrod.livesql.util.BoxUtil;

public class BinaryCaseWhenStage {

  private BinaryCaseClause clause;

  public BinaryCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralBinaryExpression value) {
    this.clause = new BinaryCaseClause(predicate, value);
  }

  // Same stage

  public BinaryCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralBinaryExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public BinaryCaseWhenStage when(final GeneralBooleanExpression predicate, final byte[] value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public BinaryCaseElseStage elseValue(final GeneralBinaryExpression value) {
    this.clause.setElse(value);
    return new BinaryCaseElseStage(this.clause);
  }

  public BinaryCaseElseStage elseValue(final byte[] value) {
    this.clause.setElse(BoxUtil.box(value));
    return new BinaryCaseElseStage(this.clause);
  }

  // Finishes the clause

  public BinaryExpression end() {
    return this.clause;
  }

}
