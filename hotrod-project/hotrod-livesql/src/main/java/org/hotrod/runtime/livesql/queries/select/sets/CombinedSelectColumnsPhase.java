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

//  private CombinedMultiSet<R> cm;

  // Constructor

  public CombinedSelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final CombinedSelectObject<R> cm, final ResultSetColumn... resultSetColumns) {
    super(context, new SelectObject<R>(ctes, distinct, false));
//    this.cm = cm;
    this.select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next stages

  public CombinedSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new CombinedSelectFromPhase<R>(this.context, this.select, tableViewOrSubquery);

  }

  // Set Operators - Inline

  // .select() .selectDistinct()

  @Override
  public CombinedSelectLinkingPhase<R> union() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new UnionOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> unionAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new UnionAllOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> except() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new ExceptOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> exceptAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new ExceptAllOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> intersect() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new IntersectOperator<>());
  }

  @Override
  public CombinedSelectLinkingPhase<R> intersectAll() {
    return new CombinedSelectLinkingPhase<>(this.context, this.select.getParent(), new IntersectAllOperator<>());
  }

  // Set Operators - Enclosed

  // .union(select()...)
  // .union(selectDistinct()...)

}
