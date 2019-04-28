package sql;

import java.util.Arrays;
import java.util.List;

import com.sun.rowset.internal.Row;

import sql.expressions.ResultSetColumn;
import sql.metadata.TableOrView;
import sql.sqldialects.SQLDialect;

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

  public List<Row> execute() {
    return this.select.execute();
  }

}
