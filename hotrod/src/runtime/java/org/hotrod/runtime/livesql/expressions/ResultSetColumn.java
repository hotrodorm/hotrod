package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

/**
 * <pre>
 *                    <I> ResultSetColumn
 *                          ^      ^        
 *                          |      |
 *  <I> ReferenceableExpression  Expression
 *           ^               ^    ^      ^
 *           |               |    |      |   
 *      AliasedExpression    Column     ...
 *
 *    
 * SelectSubquery<ReferenceableExpression>
 * Select<ResultSetColumn>
 * 
 * </pre>
 */

public interface ResultSetColumn {

  void renderTo(QueryWriter w);

}
