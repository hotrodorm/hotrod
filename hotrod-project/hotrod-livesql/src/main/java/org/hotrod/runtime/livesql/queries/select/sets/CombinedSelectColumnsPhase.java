package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.SelectObject;
import org.hotrod.runtime.livesql.queries.select.TableExpression;

public class CombinedSelectColumnsPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  public CombinedSelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    super(context, new SelectObject<R>(ctes, distinct, false));
    this.select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next stages

  public CombinedSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new CombinedSelectFromPhase<R>(this.context, this.select, tableViewOrSubquery);
  }

  // Set Operatiors - Inline

  // .select() .selectDistinct()

  public CombinedSelectLinkingPhase<R> union() {
    UnionOperator<R> op = new UnionOperator<R>();
    SetOperator<R> combinedOp = this.select.getParentOperator().combine(op);
    return new CombinedSelectLinkingPhase<R>(this.context, combinedOp);
  }

  public CombinedSelectLinkingPhase<R> unionAll() {
    UnionAllOperator<R> op = new UnionAllOperator<R>();
    SetOperator<R> combinedOp = this.select.getParentOperator().combine(op);
    return new CombinedSelectLinkingPhase<R>(this.context, combinedOp);
  }

  public CombinedSelectLinkingPhase<R> except() {
    ExceptOperator<R> op = new ExceptOperator<R>();
    SetOperator<R> combinedOp = this.select.getParentOperator().combine(op);
    return new CombinedSelectLinkingPhase<R>(this.context, combinedOp);
  }

  public CombinedSelectLinkingPhase<R> exceptAll() {
    ExceptAllOperator<R> op = new ExceptAllOperator<R>();
    SetOperator<R> combinedOp = this.select.getParentOperator().combine(op);
    return new CombinedSelectLinkingPhase<R>(this.context, combinedOp);
  }

  public CombinedSelectLinkingPhase<R> intersect() {
    IntersectOperator<R> op = new IntersectOperator<R>();
    SetOperator<R> combinedOp = this.select.getParentOperator().combine(op);
    return new CombinedSelectLinkingPhase<R>(this.context, combinedOp);
  }

  public CombinedSelectLinkingPhase<R> intersectAll() {
    IntersectAllOperator<R> op = new IntersectAllOperator<R>();
    SetOperator<R> combinedOp = this.select.getParentOperator().combine(op);
    return new CombinedSelectLinkingPhase<R>(this.context, combinedOp);
  }

  // Set Operators - Enclosed

  // .union(select()...)
  // .union(selectDistinct()...)

}
