package org.hotrod.livesql.expressions.caseclause;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.BooleanExpression;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.queries.QueryWriter;

/**
 * <pre>
 * 
 *   SQL.caseWhen(predicate, expression) -- CaseWhenStage
 *   .when(predicate, expression) -- CaseWhenStage
 *   .elseValue(expression) -- CaseElseStage
 *   .end() -- Expression
 * 
 *   SQL.caseWhen(predicate, expression) -- CaseWhenStage
 *   .end() -- Expression
 * 
 * </pre>
 * 
 * @author valarcon
 *
 */
public class BooleanCaseClause extends BooleanSyntaxExpression {

  private List<CaseWhen> whens;
  private BooleanExpression elseValue;

  public BooleanCaseClause(final BooleanExpression predicate, final BooleanExpression value) {
    super(Expression.PRECEDENCE_CASE);
    this.whens = new ArrayList<CaseWhen>();
    this.whens.add(new CaseWhen(predicate, value));
    this.elseValue = null;
    super.register(predicate);
    super.register(value);
  }

  void addWhen(final BooleanExpression predicate, final BooleanExpression value) {
    this.whens.add(new CaseWhen(predicate, value));
    super.register(predicate);
    super.register(value);
  }

  void setElse(final BooleanExpression value) {
    this.elseValue = value;
    super.register(value);
  }

  // When

  private static class CaseWhen {

    private BooleanExpression predicate;
    private BooleanExpression value;

    public CaseWhen(final BooleanExpression predicate, final BooleanExpression value) {
      this.predicate = predicate;
      this.value = value;
    }

    // Getters

    BooleanExpression getPredicate() {
      return predicate;
    }

    BooleanExpression getValue() {
      return value;
    }

  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.write("case");
    for (CaseWhen when : this.whens) {
      w.write(" when ");
      super.renderInner(when.getPredicate(), w);
      w.write(" then ");
      super.renderInner(when.getValue(), w);
    }
    if (this.elseValue != null) {
      w.write(" else ");
      super.renderInner(this.elseValue, w);
    }
    w.write(" end");
  }

}
