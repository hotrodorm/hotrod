package org.hotrod.runtime.livesql.expressions;

/**
 * <pre>
 *                      {I} Rendereable
 *                             ^
 *                             | 
 *                    {I} ResultSetColumn &lt;------+----------------+
 *                          ^      ^             |                |
 *                          |      |             |                |
 *  {I} ReferenceableExpression  Expression  AllColumns   {I} ColumnList
 *           ^               ^    ^      ^                      ^    ^
 *           |               |    |      |                      |    |
 *      AliasedExpression    Column   [Many...]      ColumnAliased  ColumnSubset
 *
 * </pre>
 */

public interface ResultSetColumn extends Rendereable {

}
