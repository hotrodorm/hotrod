package sql.expressions;

import sql.QueryWriter;

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
