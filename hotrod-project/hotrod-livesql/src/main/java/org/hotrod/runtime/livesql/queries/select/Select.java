package org.hotrod.runtime.livesql.queries.select;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.TableOrView;

class Select<R> extends AbstractSelect<R> {

  private List<ResultSetColumn> resultSetColumns = new ArrayList<>();

  Select(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper) {
    super(sqlDialect, distinct, sqlSession, null, liveSQLMapper);
  }

  Select(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final String mapperStatement) {
    super(sqlDialect, distinct, sqlSession, mapperStatement, null);
  }

  // Setters

  void setResultSetColumns(final List<ResultSetColumn> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected void writeColumns(final QueryWriter w, final TableOrView baseTable, final List<Join> joins) {
    super.writeExpandedColumns(w, baseTable, joins, this.resultSetColumns);
  }

}
