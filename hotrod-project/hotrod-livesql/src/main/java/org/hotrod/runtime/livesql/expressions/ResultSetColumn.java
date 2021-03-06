package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

/**
 * <pre>
 *                    {I} ResultSetColumn
 *                          ^      ^        
 *                          |      |
 *  {I} ReferenceableExpression  Expression
 *           ^               ^    ^      ^
 *           |               |    |      |   
 *      AliasedExpression    Column     ...
 *
 *    
 * SelectSubquery&lt;ReferenceableExpression&gt;
 * Select&lt;ResultSetColumn&gt;
 * 
 * </pre>
 */

public interface ResultSetColumn {

  void renderTo(QueryWriter w);

}
