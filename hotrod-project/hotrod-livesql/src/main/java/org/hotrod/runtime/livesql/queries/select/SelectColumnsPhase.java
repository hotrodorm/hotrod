package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.sets.AbstractSelectPhase;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectLinkingPhase;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectPhase;
import org.hotrod.runtime.livesql.queries.select.sets.ExceptAllOperator;
import org.hotrod.runtime.livesql.queries.select.sets.ExceptOperator;
import org.hotrod.runtime.livesql.queries.select.sets.IntersectAllOperator;
import org.hotrod.runtime.livesql.queries.select.sets.IntersectOperator;
import org.hotrod.runtime.livesql.queries.select.sets.UnionAllOperator;
import org.hotrod.runtime.livesql.queries.select.sets.UnionOperator;

public class SelectColumnsPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  public SelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    super(context, new SelectObject<R>(ctes, distinct, false));
    for (ResultSetColumn c : resultSetColumns) {
      if (c == null) {
        throw new LiveSQLException("Select column cannot be null.");
      }
    }
    this.select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next stages

  public SelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new SelectFromPhase<R>(this.context, this.select, tableViewOrSubquery);
  }

  // Set Operatiors - Inline

  // .union().select()...
  // .union().selectDistinct()...

  public CombinedSelectLinkingPhase<R> union() {
    UnionOperator<R> op = new UnionOperator<R>();
    op.add(this.select);
    return new CombinedSelectLinkingPhase<R>(this.context, op);
  }

  public CombinedSelectLinkingPhase<R> unionAll() {
    UnionAllOperator<R> op = new UnionAllOperator<R>();
    op.add(this.select);
    return new CombinedSelectLinkingPhase<R>(this.context, op);
  }

  public CombinedSelectLinkingPhase<R> except() {
    ExceptOperator<R> op = new ExceptOperator<R>();
    op.add(this.select);
    return new CombinedSelectLinkingPhase<R>(this.context, op);
  }

  public CombinedSelectLinkingPhase<R> exceptAll() {
    ExceptAllOperator<R> op = new ExceptAllOperator<R>();
    op.add(this.select);
    return new CombinedSelectLinkingPhase<R>(this.context, op);
  }

  public CombinedSelectLinkingPhase<R> intersect() {
    IntersectOperator<R> op = new IntersectOperator<R>();
    op.add(this.select);
    return new CombinedSelectLinkingPhase<R>(this.context, op);
  }

  public CombinedSelectLinkingPhase<R> intersectAll() {
    IntersectAllOperator<R> op = new IntersectAllOperator<R>();
    op.add(this.select);
    return new CombinedSelectLinkingPhase<R>(this.context, op);
  }

  // Set Operators - Enclosed

  // .union(select()...)
  // .union(selectDistinct()...)

  public CombinedSelectPhase<R> union(final Select<R> select) {
    UnionOperator<R> op = new UnionOperator<R>();
    op.add(this.select);
    op.add(select.getSelect());
    return new CombinedSelectPhase<R>(op);
  }

}
