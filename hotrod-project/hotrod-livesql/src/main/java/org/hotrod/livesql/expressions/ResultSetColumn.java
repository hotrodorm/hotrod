package org.hotrod.livesql.expressions;

import java.util.List;

/**
 * <pre>
 *                ResultSetColumn (expand)
 *               /               \
 *              /                 \
 *  WrappingColumn                Expression (as)   {I} OrderingTerm
 *  |  |  |                      /       |  \             /      \
 *  |  |  AllColumns   AliasedExpression |   \           /     OrderingExpression ()
 *  |  |                                 |    \         /
 *  |  ColumnList           TypedExpression    \       /          
 *  |    |  |                                   \     /
 *  |    |  ColumnsSubset                 ExistenceExpression (isNull,isNotNull)
 *  |    |                                         |
 *  |    ColumnsAliased                            |
 *  |                                    SortableExpression (asc/desc)
 *  AllSubqueryColumns                             |
 *                                                 |
 *                                       EquatableExpression (=All,=Any,<>All,<>Any,in,not in)
 *                                                 |   \
 *                                                 |  ConvertedColumn (coalesce,=,in,<>,not in,nullif)
 *                                                 |
 *                                       ComparableExpression (<All,>All,<=All,>=All,<Any,>Any,<=Any,>=Any)
 *                                               /    \
 *                                              /      \
 *                              {I} Column     /        \    {I} SubqueryColumn
 *                                      \     /          \        /
 *                                     TTTColumn        SubqueryTTTColumn
 * 
 * </pre>
 */

public abstract class ResultSetColumn {

  protected abstract Expression getEmergingExpression();

  protected abstract List<Expression> expand();

}
