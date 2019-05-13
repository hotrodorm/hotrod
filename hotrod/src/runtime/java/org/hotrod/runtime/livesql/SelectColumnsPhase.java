package org.hotrod.runtime.livesql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class SelectColumnsPhase implements ExecutableSelect {

  // Properties

  private AbstractSelect<Map<String, Object>> select;

  // Constructor

  public SelectColumnsPhase(final SQLDialect sqlDialect, final SqlSession sqlSession, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    Select<Map<String, Object>> s = new Select<Map<String, Object>>(sqlDialect, distinct, sqlSession);
    s.setResultSetColumns(Arrays.asList(resultSetColumns));
    this.select = s;
  }

  // Next stages

  public SelectFromPhase from(final TableOrView t) {
    return new SelectFromPhase(this.select, t);
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

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.select.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.select.designateAliases(ag);
  }

}
