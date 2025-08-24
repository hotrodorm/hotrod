package org.hotrod.livesql.expressions;

import java.util.List;

import org.hotrod.livesql.util.ToString;

/*
 * <pre>
 *               SQLExpression (expand)
 *               /               \
 *              /                 \
 *  MetaExpression                Expression                      {I} OrderingTerm
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
 *                                            EquatableExpression (=All,=Any,<>All,<>Any,in,notIn)
 *                                                      |   \
 *                                                      |  ConvertedColumn (coalesce,=,in,<>,notIn,nullIf)
 *                                                      |
 *                                            ComparableExpression (<All,>All,<=All,>=All,<Any,>Any,<=Any,>=Any)
 *                                                |
 *                                                +- NumericExpression
 *                                                |  +- NumericEntityColumn   -> {I} EntityColumn 
 *                                                |  +- NumericSyntaxExpression
 *                                                |     +- NumericSubqueryExpression -> {I} SubqueryExpression
 *                                                +- CharExpression
 *                                                |  +- CharEntityColumn      -> {I} EntityColumn
 *                                                |  +- CharSyntaxExpression
 *                                                |     +- CharSubqueryExpression -> {I} SubqueryExpression
 *                                                +- DateTimeExpression
 *                                                |  +- DateTimeEntityColumn  -> {I} EntityColumn
 *                                                |  +- DateTimeSyntaxExpression
 *                                                |     +- DateTimeSubqueryExpression -> {I} SubqueryExpression
 *                                                +- BooleanExpression
 *                                                |  +- BooleanEntityColumn   -> {I} EntityColumn
 *                                                |  +- BooleanSyntaxExpression
 *                                                |     +- BooleanSubqueryExpression -> {I} SubqueryExpression
 *                                                +- BinaryExpression
 *                                                |  +- BinaryEntityColumn    -> {I} EntityColumn
 *                                                |  +- BinarySyntaxExpression
 *                                                |     +- BinarySubqueryExpression -> {I} SubqueryExpression
 *                                                +- ObjectExpression
 *                                                   +- ObjectEntityColumn    -> {I} EntityColumn
 *                                                   +- ObjectSyntaxExpression
 *                                                      +- ObjectSubqueryExpression -> {I} SubqueryExpression
 *                                          
 * 
 * </pre>
 */

public abstract class SQLExpression extends LiveSQLExpression {

  protected abstract List<Expression> expand();

  protected abstract void log(ToString t);

}
