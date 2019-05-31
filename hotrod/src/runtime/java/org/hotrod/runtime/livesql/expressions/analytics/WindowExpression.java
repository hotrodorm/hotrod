package org.hotrod.runtime.livesql.expressions.analytics;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
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

  public enum FrameUnit {
    ROWS("rows"), //
    RANGE("range"), //
    GROUPS("groups");

    private String caption;

    private FrameUnit(final String caption) {
      this.caption = caption;
    }

    public String render() {
      return this.caption;
    }

  }

  public enum FrameBound {
    UNBOUNDED_PRECEDING("unbounded preceding"), //
    OFFSET_PRECEDING("preceding"), //
    CURRENT_ROW("current row"), //
    OFFSET_FOLLOWING("following"), //
    UNBOUNDED_FOLLOWING("unbounded following");

    private String caption;

    private FrameBound(final String caption) {
      this.caption = caption;
    }

    public String render(final Integer offset) {
      StringBuilder sb = new StringBuilder();
      if (this == OFFSET_PRECEDING || this == OFFSET_FOLLOWING) {
        sb.append("" + offset + " ");
      }
      sb.append(this.caption);
      return sb.toString();
    }

  }

  public enum FrameExclusion {
    EXCLUDE_CURRENT_ROW("exclude current row"), //
    EXCLUDE_GROUP("exclude group"), //
    EXCLUDE_TIES("exclude ties"), //
    EXCLUDE_NO_OTHERS("exclude no others");

    private String caption;

    private FrameExclusion(final String caption) {
      this.caption = caption;
    }

    public String render() {
      return this.caption;
    }

  }

  // Properties

  private WindowableFunction<T> windowablefunction;
  private List<Expression<?>> partitionBy;
  private List<OrderingTerm> orderBy;

  private FrameUnit frameUnit;
  private FrameBound frameStart;
  private Integer offsetStart;
  private FrameBound frameEnd;
  private Integer offsetEnd;
  private FrameExclusion frameExclusion;

  // Constructor

  public WindowExpression(final WindowableFunction<T> windowablefunction) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.windowablefunction = windowablefunction;
    this.partitionBy = null;
    this.orderBy = null;
    this.frameUnit = null;
    this.frameStart = null;
    this.offsetStart = null;
    this.frameEnd = null;
    this.offsetEnd = null;
    this.frameExclusion = null;
  }

  // Setters

  void setPartitionBy(final List<Expression<?>> partitionBy) {
    this.partitionBy = partitionBy;
  }

  void setOrderBy(final List<OrderingTerm> orderBy) {
    this.orderBy = orderBy;
  }

  void setFrameUnit(final FrameUnit frameUnit) {
    this.frameUnit = frameUnit;
  }

  void setFrameStart(final FrameBound frameStart, final Integer offsetStart) {
    this.frameStart = frameStart;
    this.offsetStart = offsetStart;
  }

  void setFrameEnd(final FrameBound frameEnd, final Integer offsetEnd) {
    this.frameEnd = frameEnd;
    this.offsetEnd = offsetEnd;
  }

  void setFrameExclusion(final FrameExclusion frameExclusion) {
    this.frameExclusion = frameExclusion;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {

    this.windowablefunction.renderBaseTo(w);

    w.write(" over(");

    boolean hasPartitionBy = this.partitionBy != null && !this.partitionBy.isEmpty();
    boolean hasOrderBy = this.orderBy != null && !this.orderBy.isEmpty();

    // partition by

    if (hasPartitionBy) {
      w.write("partition by ");
      Separator sep = new Separator();
      for (Expression<?> expr : this.partitionBy) {
        w.write(sep.render());
        this.renderInner(expr, w);
      }
    }

    // order by

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

    // framing

    if (this.frameUnit != null) {
      w.write(" " + this.frameUnit.render() + " ");
      if (this.frameEnd == null) { // start only
        w.write(this.frameStart.render(this.offsetStart));
      } else { // between
        w.write("between ");
        w.write(this.frameStart.render(this.offsetStart));
        w.write(" and ");
        w.write(this.frameEnd.render(this.offsetEnd));
      }
      if (this.frameExclusion != null) {
        w.write(" " + this.frameExclusion.render());
      }
    }

    w.write(")");

  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    for (Expression<?> e : this.partitionBy) {
      e.validateTableReferences(tableReferences, ag);
    }
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    for (Expression<?> e : this.partitionBy) {
      e.designateAliases(ag);
    }
  }

}
