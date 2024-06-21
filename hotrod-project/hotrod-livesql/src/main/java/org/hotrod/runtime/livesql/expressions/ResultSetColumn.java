package org.hotrod.runtime.livesql.expressions;

/**
 * <pre>
 *                      {I} Rendereable
 *                             ^
 *                             | 
 *                    {I} ResultSetColumn &lt;------+----------------+---------------+
 *                          ^      ^             |                |               |
 *                          |      |             |                |               |
 *  {I} ReferenceableExpression  Expression  AllColumns   {I} ColumnList   AllSubqueryColumns
 *             ^          ^      ^  ^  ^  ^                      ^    ^
 *             |          |      |  |  |  |                      |    |
 *             |    {I} Column   |  |  |  [Many...]  ColumnsAliased  ColumnsSubset
 *             |            ^    |  |  |
 *             |            |    |  |  |
 *             |         ...Column  |  |
 *             |                    |  |
 *             | {I} SubqueryColumn |  |
 *             |         ^          |  |
 *             |         |          |  |
 *             |    Subquery...Column  |
 *             |                       |
 *             +---AliasedExpression---+
 * </pre>
 */

public interface ResultSetColumn extends Rendereable {
  
}
