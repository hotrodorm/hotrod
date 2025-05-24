package org.hotrod.livesql.expressions.caseclause;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.expressions.predicates.Predicate;
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
public class BooleanCaseClause extends Predicate {

  private List<CaseWhen> whens;
  private GeneralBooleanExpression elseValue;

  public BooleanCaseClause(final GeneralBooleanExpression predicate, final GeneralBooleanExpression value) {
    super(Expression.PRECEDENCE_CASE);
    this.whens = new ArrayList<CaseWhen>();
    this.whens.add(new CaseWhen(predicate, value));
    this.elseValue = null;
    super.register(predicate);
    super.register(value);
  }

  void addWhen(final GeneralBooleanExpression predicate, final GeneralBooleanExpression value) {
    this.whens.add(new CaseWhen(predicate, value));
    super.register(predicate);
    super.register(value);
  }

  void setElse(final GeneralBooleanExpression value) {
    this.elseValue = value;
    super.register(value);
  }

  // When

  private static class CaseWhen {

    private GeneralBooleanExpression predicate;
    private GeneralBooleanExpression value;

    public CaseWhen(final GeneralBooleanExpression predicate, final GeneralBooleanExpression value) {
      this.predicate = predicate;
      this.value = value;
    }

    // Getters

    GeneralBooleanExpression getPredicate() {
      return predicate;
    }

    GeneralBooleanExpression getValue() {
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
