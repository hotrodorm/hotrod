package org.hotrod.runtime.livesql.expressions;

/**
 * <pre>
 *                    {I} ResultSetColumn &lt;-------- WrappingColumn &lt;---+---------------+
 *                          ^      ^                  ^                |               |
 *                          |      |                  |                |               |
 *                          |      | {I} Rendereable  |                |               |
 *                          |      |      ^           |                |               |
 *                          |      |      |           |                |               |
 *  {I} ReferenceableExpression  Expression      AllColumns        ColumnList   AllSubqueryColumns
 *             ^          ^      ^  ^  ^  ^                          ^    ^
 *             |          |      |  |  |  |                          |    |
 *             |    {I} Column   |  |  |  [Many...]      ColumnsAliased  ColumnsSubset
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

public interface ResultSetColumn {

}
