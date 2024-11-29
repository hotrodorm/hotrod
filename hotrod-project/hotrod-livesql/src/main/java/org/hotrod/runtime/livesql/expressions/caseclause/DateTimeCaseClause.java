package org.hotrod.runtime.livesql.expressions.caseclause;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.GeneralDateTimeExpression;
import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

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
public class DateTimeCaseClause extends DateTimeExpression {

  private List<CaseWhen> whens;
  private GeneralDateTimeExpression elseValue;

  public DateTimeCaseClause(final GeneralBooleanExpression predicate, final GeneralDateTimeExpression value) {
    super(Expression.PRECEDENCE_CASE);
    this.whens = new ArrayList<CaseWhen>();
    this.whens.add(new CaseWhen(predicate, value));
    this.elseValue = null;
    super.register(predicate);
    super.register(value);
  }

  void addWhen(final GeneralBooleanExpression predicate, final GeneralDateTimeExpression value) {
    this.whens.add(new CaseWhen(predicate, value));
    super.register(predicate);
    super.register(value);
  }

  void setElse(final GeneralDateTimeExpression value) {
    this.elseValue = value;
    super.register(value);
  }

  // When

  private static class CaseWhen {

    private GeneralBooleanExpression predicate;
    private GeneralDateTimeExpression value;

    public CaseWhen(final GeneralBooleanExpression predicate, final GeneralDateTimeExpression value) {
      this.predicate = predicate;
      this.value = value;
    }

    // Getters

    GeneralBooleanExpression getPredicate() {
      return predicate;
    }

    GeneralDateTimeExpression getValue() {
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
