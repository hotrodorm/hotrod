package org.hotrod.runtime.livesql.queries.select;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class Subquery extends AbstractSelect<Map<String, Object>> {

  private List<ReferenceableExpression> resultSetColumns = null;

  Subquery(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession,
      final LiveSQLMapper liveSQLMapper) {
    super(sqlDialect, distinct, sqlSession, null, liveSQLMapper);
  }

  // Setters

  void setResultSetColumns(final List<ReferenceableExpression> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected void writeColumns(final QueryWriter w, final TableOrView baseTable, final List<Join> joins) {
    super.writeExpandedColumns(w, baseTable, joins, this.resultSetColumns.stream().collect(Collectors.toList()), false);
  }

}
