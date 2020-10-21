package org.hotrod.runtime.livesql.expressions.caseclause;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
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
 * @param <T> The type of the evaluated expression
 */
public class CaseClause<T> extends Expression<T> {

  private List<CaseWhen<T>> whens;
  private Expression<T> elseValue;

  public CaseClause(final Predicate predicate, final Expression<T> value) {
    super(Expression.PRECEDENCE_CASE);
    this.whens = new ArrayList<CaseWhen<T>>();
    this.whens.add(new CaseWhen<T>(predicate, value));
    this.elseValue = null;
    super.register(predicate);
    super.register(value);
  }

  void addWhen(final Predicate predicate, final Expression<T> value) {
    this.whens.add(new CaseWhen<T>(predicate, value));
    super.register(predicate);
    super.register(value);
  }

  void setElse(final Expression<T> value) {
    this.elseValue = value;
    super.register(value);
  }

  // When

  private static class CaseWhen<T> {

    private Predicate predicate;
    private Expression<T> value;

    public CaseWhen(final Predicate predicate, final Expression<T> value) {
      this.predicate = predicate;
      this.value = value;
    }

    // Getters

    Predicate getPredicate() {
      return predicate;
    }

    Expression<T> getValue() {
      return value;
    }

  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("case");
    for (CaseWhen<T> when : this.whens) {
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
