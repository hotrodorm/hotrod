package org.hotrod.runtime.livesql.expressions;

import java.util.List;

/**
 * <pre>
 *               ResultSetColumn (unwrap)
 *               /             \
 *              /               \
 *  WrappingColumn              Expression (rt)   {I} OrderingTerm
 *  |  |  |                      /       \             /      \
 *  |  |  AllColumns   AliasedExpression  \           /     OrderingExpression (rt)
 *  |  |                                   \         /
 *  |  ColumnList                      GenericExpression (isNull,isNotNull,as)
 *  |    |  |                                 |
 *  |    |  ColumnsSubset                     |
 *  |    ColumnsAliased                       |
 *  |                                         |
 *  AllSubqueryColumns              ComparableExpression (asc/desc,=,<>,<,>,<=,>=,...)
 *                                         /      \
 *                         {I} Column     /        \    {I} SubqueryColumn
 *                                 \     /          \        /
 *                                TTTColumn        SubqueryTTTColumn
 * 
 * </pre>
 */

public abstract class ResultSetColumn {

  protected abstract Expression getExpression();

  protected abstract List<Expression> unwrap();

}
