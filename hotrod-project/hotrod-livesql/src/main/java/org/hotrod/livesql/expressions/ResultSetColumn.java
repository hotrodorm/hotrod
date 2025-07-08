package org.hotrod.livesql.expressions;

import java.util.List;

import org.hotrod.livesql.util.ToString;

/**
 * <pre>
 *                ResultSetColumn (expand)
 *               /               \
 *              /                 \
 *  WrappingColumn                Expression                      {I} OrderingTerm
 *  |  |  |                      /       |  \                           /      \
 *  |  |  AllColumns   AliasedExpression |   \                         /     OrderingExpression ()
 *  |  |                                 |  UnaliasedExpression (as)  /
 *  |  ColumnList           TypedExpression    \                     /          
 *  |    |  |                                   \                   /
 *  |    |  ColumnsSubset                        ExistenceExpression (isNull,isNotNull)
 *  |    |                                              |
 *  |    ColumnsAliased                                 |
 *  |                                         SortableExpression (asc/desc)
 *  AllSubqueryColumns                                  |
 *                                                      |
 *                                            EquatableExpression (=All,=Any,<>All,<>Any,in,not in)
 *                                                      |   \
 *                                                      |  ConvertedColumn (coalesce,=,in,<>,not in,nullif)
 *                                                      |
 *                                            ComparableExpression (<All,>All,<=All,>=All,<Any,>Any,<=Any,>=Any)
 *                                                    /    \
 *                                                   /      \
 *                                   {I} Column     /        \    {I} SubqueryColumn
 *                                           \     /          \        /
 *                                          TTTColumn        SubqueryTTTColumn
 * 
 * </pre>
 */

public abstract class ResultSetColumn {

  protected abstract List<Expression> expand();

  protected abstract void log(ToString t);

}
