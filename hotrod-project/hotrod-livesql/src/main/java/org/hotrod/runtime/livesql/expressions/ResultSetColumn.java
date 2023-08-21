package org.hotrod.runtime.livesql.expressions;

/**
 * <pre>
 *                    {I} ResultSetColumn <------+----------------+
 *                          ^      ^             |                |
 *                          |      |             |                |
 *  {I} ReferenceableExpression  Expression  AllColumns   {I} ColumnList
 *           ^               ^    ^      ^                      ^    ^
 *           |               |    |      |                      |    |
 *      AliasedExpression    Column     ...          ColumnAliased  ColumnSubset
 *
 *    
 * SelectSubquery&lt;ReferenceableExpression&gt;
 * Select&lt;ResultSetColumn&gt;
 * 
 * </pre>
 */

public interface ResultSetColumn extends Rendereable {

}
