package org.hotrod.runtime.livesql.expressions.caseclause;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

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
public class NumberCaseClause extends NumberExpression {

  private List<CaseWhen> whens;
  private NumberExpression elseValue;

  public NumberCaseClause(final Predicate predicate, final NumberExpression value) {
    super(Expression.PRECEDENCE_CASE);
    this.whens = new ArrayList<CaseWhen>();
    this.whens.add(new CaseWhen(predicate, value));
    this.elseValue = null;
    super.register(predicate);
    super.register(value);
  }

  void addWhen(final Predicate predicate, final NumberExpression value) {
    this.whens.add(new CaseWhen(predicate, value));
    super.register(predicate);
    super.register(value);
  }

  void setElse(final NumberExpression value) {
    this.elseValue = value;
    super.register(value);
  }

  // When

  private static class CaseWhen {

    private Predicate predicate;
    private NumberExpression value;

    public CaseWhen(final Predicate predicate, final NumberExpression value) {
      this.predicate = predicate;
      this.value = value;
    }

    // Getters

    Predicate getPredicate() {
      return predicate;
    }

    NumberExpression getValue() {
      return value;
    }

  }

  @Override
  public void renderTo(final QueryWriter w) {
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
