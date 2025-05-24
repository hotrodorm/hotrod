package org.hotrod.livesql.expressions.caseclause;

import org.hotrod.livesql.expressions.binary.ByteArrayExpression;
import org.hotrod.livesql.expressions.binary.GeneralByteArrayExpression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.util.BoxUtil;

public class ByteArrayCaseWhenStage {

  private ByteArrayCaseClause clause;

  public ByteArrayCaseWhenStage(final GeneralBooleanExpression predicate, final GeneralByteArrayExpression value) {
    this.clause = new ByteArrayCaseClause(predicate, value);
  }

  // Same stage

  public ByteArrayCaseWhenStage when(final GeneralBooleanExpression predicate, final GeneralByteArrayExpression value) {
    this.clause.addWhen(predicate, value);
    return this;
  }

  public ByteArrayCaseWhenStage when(final GeneralBooleanExpression predicate, final byte[] value) {
    this.clause.addWhen(predicate, BoxUtil.box(value));
    return this;
  }

  // Next stages

  public ByteArrayCaseElseStage elseValue(final GeneralByteArrayExpression value) {
    this.clause.setElse(value);
    return new ByteArrayCaseElseStage(this.clause);
  }

  public ByteArrayCaseElseStage elseValue(final byte[] value) {
    this.clause.setElse(BoxUtil.box(value));
    return new ByteArrayCaseElseStage(this.clause);
  }

  // Finishes the clause

  public ByteArrayExpression end() {
    return this.clause;
  }

}
