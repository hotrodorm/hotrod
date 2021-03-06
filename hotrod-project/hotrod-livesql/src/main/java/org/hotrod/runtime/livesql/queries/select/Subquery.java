package org.hotrod.runtime.livesql.queries.select;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

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
  protected void writeColumns(final QueryWriter w) {
    if (this.resultSetColumns == null || this.resultSetColumns.isEmpty()) {
      w.write("\n  *");
    } else {
      boolean first = true;
      for (ResultSetColumn c : this.resultSetColumns) {
        if (first) {
          first = false;
        } else {
          w.write(",");
        }
        w.write("\n  ");
        c.renderTo(w);
      }
    }
  }

}
