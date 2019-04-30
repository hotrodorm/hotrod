package org.hotrod.runtime.sql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.sql.dialects.SQLDialect;
import org.hotrod.runtime.sql.expressions.ResultSetColumn;
import org.hotrod.runtime.sql.metadata.TableOrView;

public class SelectColumns implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructor

  SelectColumns(final SQLDialect sqlDialect, final boolean distinct, final ResultSetColumn... resultSetColumns) {
    Select s = new Select(sqlDialect, distinct);
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
