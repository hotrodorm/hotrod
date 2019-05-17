package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.util.Separator;

class Select<T> extends AbstractSelect<T> {

  private List<ResultSetColumn> resultSetColumns = null;

  Select(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession) {
    super(sqlDialect, distinct, sqlSession, null);
  }

  Select(final SQLDialect sqlDialect, final boolean distinct, final SqlSession sqlSession, final String statement) {
    super(sqlDialect, distinct, sqlSession, statement);
  }

  // Setters

  void setResultSetColumns(final List<ResultSetColumn> resultSetColumns) {
    this.resultSetColumns = resultSetColumns;
  }

  // Rendering

  @Override
  protected void writeColumns(final QueryWriter w) {
    if (this.resultSetColumns == null || this.resultSetColumns.isEmpty()) {
      w.write("\n  *");
    } else {
      Separator sep = new Separator();
      for (ResultSetColumn c : this.resultSetColumns) {
        w.write(sep.render());
        w.write("\n  ");
        c.renderTo(w);

        try {
          Column col = (Column) c;
          w.write(" as ");
          w.write(w.getSqlDialect().getIdentifierRenderer().renderSQLName(col.getProperty()));
        } catch (ClassCastException e) {
          // Not a property -- no need to alias it
        }

      }
    }
  }

}
