package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

class PGSelect<R> extends AbstractSelect<R> {

  private boolean doNotAliasColumns;
  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  PGSelect(final LiveSQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper, final boolean doNotAliasColumns) {
    super(sqlDialect, distinct, sqlSession, null, liveSQLMapper);
    this.doNotAliasColumns = doNotAliasColumns;
  }

  PGSelect(final LiveSQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final String mapperStatement, final boolean doNotAliasColumns) {
    super(sqlDialect, distinct, sqlSession, mapperStatement, null);
    this.doNotAliasColumns = doNotAliasColumns;
  }

  // Setters

  void setResultSetColumns(final List<ResultSetColumn> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    super.writeExpandedColumns(w, baseTableExpression, joins, this.resultSetColumns, this.doNotAliasColumns);
  }

  @Override
  public List<ResultSetColumn> listColumns() {
    return this.resultSetColumns;
  }

}
