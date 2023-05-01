package org.hotrod.runtime.livesql.queries.select;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.TableOrView;

class CombinedSelect<T> extends AbstractSelect<T> {

  private List<ResultSetColumn> resultSetColumns = null;

  CombinedSelect(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper) {
    super(sqlDialect, distinct, sqlSession, null, liveSQLMapper);
  }

  CombinedSelect(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
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
    super.writeExpandedColumns(w, baseTable, joins, this.resultSetColumns.stream().collect(Collectors.toList()));
  }

}
