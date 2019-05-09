package org.hotrod.runtime.livesql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class SelectColumns implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructor

  public SelectColumns(final SQLDialect sqlDialect, final SqlSession sqlSession, final boolean distinct, final ResultSetColumn... resultSetColumns) {
    Select s = new Select(sqlDialect, distinct, sqlSession);
    s.setResultSetColumns(Arrays.asList(resultSetColumns));
    this.select = s;
  }

  // Next stages

  public SelectFrom from(final TableOrView t) {
    return new SelectFrom(this.select, t);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<Map<String, Object>> execute() {
    return this.select.execute();
  }

}
