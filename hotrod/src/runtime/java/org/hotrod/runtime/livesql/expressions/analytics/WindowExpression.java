package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.List;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.util.Separator;

/**
 * <pre>
 * 
 *                  Expression<T>
 *                    ^       ^
 *                    |       |
 *     WindowExpression<T>   AggregationFunction<T>
 *                              ^              ^                   <I> WindowableFunction<T>         .over()
 *                              |              |                               ^  ^
 *   NonWindowableAggregationFunction<T>  WindowableAggregationFunction<T>.....:  :.....AnalyticFunction<T>
 *    ^                                    ^    ^                                          ^             ^
 *    |                                    |    |                                          |             |
 *    |            NumericAggregationFunction  StringAggregationFunction  PositionalAnalyticFunction<?>  |
 *    |                 ^                         ^                          ^                           |
 *    |- Count          |- Sum                    |- GroupConcat             |- Lead                     |- RowNumber
 *    |- CountDistinct  |- Avg                                               |- Lag                      |- Rank
 *    |- SumDistinct    |- Min                                                                           |- DenseRank
 *    |- AvgDistinct    |- Max                                                                           |- NTile
 *    |- GroupConcatDistinct
 * 
 * 
 *   SQL.sum(a.salary) -- WindowableFunction                            .over()
 *   .over() -- WindowFunctionOverStage<T>                              .partitionBy()  .orderBy()  .end()
 *   .partitionBy(expression...) -- WindowFunctionPartitioningStage<T>  .orderBy()  .end()
 *   .orderBy(expression...) -- WindowFunctionOrderingStage<T>          .end()
 *   .end() -- Expression<T>
 * 
 * </pre>
 * 
 * @param <T>
 */

public class WindowExpression<T> extends Expression<T> {

  private final static int PRECEDENCE = 1;

  // Properties

  private WindowableFunction<T> windowablefunction;
  private List<Expression<?>> partitionBy;
  private List<OrderingTerm> orderBy;

  // Constructor

  public WindowExpression(final WindowableFunction<T> windowablefunction) {
    super(PRECEDENCE);
    this.windowablefunction = windowablefunction;
    this.partitionBy = null;
    this.orderBy = null;
  }

  // Setters

  void setPartitionBy(final List<Expression<?>> partitionBy) {
    this.partitionBy = partitionBy;
  }

  void setOrderBy(final List<OrderingTerm> orderBy) {
    this.orderBy = orderBy;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {

    this.windowablefunction.renderBaseTo(w);

    w.write(" over(");

    boolean hasPartitionBy = this.partitionBy != null && !this.partitionBy.isEmpty();
    boolean hasOrderBy = this.orderBy != null && !this.orderBy.isEmpty();

    if (hasPartitionBy) {
      w.write("partition by ");
      Separator sep = new Separator();
      for (Expression<?> expr : this.partitionBy) {
        w.write(sep.render());
        this.renderInner(expr, w);
      }
    }

    if (hasOrderBy) {
      if (hasPartitionBy) {
        w.write(" ");
      }
      w.write("order by ");
      Separator sep = new Separator();
      for (OrderingTerm expr : this.orderBy) {
        w.write(sep.render());
        expr.renderTo(w);
      }
    }

    w.write(")");

  }

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    for (Expression<?> e : this.partitionBy) {
      e.gatherAliases(ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (Expression<?> e : this.partitionBy) {
      e.designateAliases(ag);
    }
  }

}
