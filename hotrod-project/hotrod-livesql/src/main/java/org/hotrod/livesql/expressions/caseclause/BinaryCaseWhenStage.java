package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.binary.BinarySyntaxExpression;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.util.BoxUtil;

public class BinaryCaseWhenStage {

  private BinaryCaseClause clause;

  public BinaryCaseWhenStage(final BooleanExpression predicate, final BinaryExpression value) {
    this.clause = new BinaryCaseClause(predicate, value);
  }

  // Same stage

  public BinaryCaseWhenStage when(final BooleanExpression predicate, final BinaryExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public BinaryCaseWhenStage when(final BooleanExpression predicate, final byte[] value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public BinaryCaseElseStage elseValue(final BinaryExpression value) {
    this.clause.setElse(value);
    return new BinaryCaseElseStage(this.clause);
  }

  public BinaryCaseElseStage elseValue(final byte[] value) {
    this.clause.setElse(BoxUtil.box(value));
    return new BinaryCaseElseStage(this.clause);
  }

  // Finishes the clause

  public BinarySyntaxExpression end() {
    return this.clause;
  }

}
