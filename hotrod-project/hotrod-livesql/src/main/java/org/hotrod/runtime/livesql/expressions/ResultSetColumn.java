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
 *                     ^    ^    ^   ^    ^                     ^    ^
 *                     |    |    |   |    |                     |    |
 *                     |    Column   |    [Many...]  ColumnAliased  ColumnSubset
 *                    AliasedExpression
 * </pre>
 */

public interface ResultSetColumn extends Rendereable {
  
}
