package org.hotrod.runtime.sql.expressions;

import org.hotrod.runtime.sql.QueryWriter;

/**
 * <pre>
 *                    <I> ResultSetColumn
 *                          ^      ^        
 *                          |      |
 *  <I> ReferenceableExpression  Expression
 *           ^               ^    ^
 *           |               |    |       
 *      AliasedExpression    Column   
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
