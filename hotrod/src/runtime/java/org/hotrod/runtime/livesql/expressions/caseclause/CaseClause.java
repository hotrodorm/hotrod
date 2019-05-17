package org.hotrod.runtime.livesql.expressions.caseclause;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

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
 * @param <T>
 */
public class CaseClause<T> extends Expression<T> {

  private static final int PRECEDENCE = 2;

  private List<CaseWhen<T>> whens;
  private Expression<T> elseValue;

  public CaseClause(final Predicate predicate, final Expression<T> value) {
    super(PRECEDENCE);
    this.whens = new ArrayList<CaseWhen<T>>();
    this.whens.add(new CaseWhen<T>(predicate, value));
    this.elseValue = null;
  }

  public void add(final Predicate predicate, final Expression<T> value) {
    this.whens.add(new CaseWhen<T>(predicate, value));
  }

  public void setElse(final Expression<T> value) {
    this.elseValue = value;
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

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    for (CaseWhen<T> w : this.whens) {
      w.predicate.validateTableReferences(tableReferences, ag);
      w.value.validateTableReferences(tableReferences, ag);
    }
    if (this.elseValue != null) {
      this.elseValue.validateTableReferences(tableReferences, ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (CaseWhen<T> w : this.whens) {
      w.predicate.designateAliases(ag);
      w.value.designateAliases(ag);
    }
    if (this.elseValue != null) {
      this.elseValue.designateAliases(ag);
    }
  }

}
