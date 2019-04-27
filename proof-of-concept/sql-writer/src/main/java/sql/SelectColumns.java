package sql;

import java.util.Arrays;
import java.util.List;

import com.sun.rowset.internal.Row;

import sql.expressions.Expression;
import sql.metadata.TableOrView;
import sql.sqldialects.SQLDialect;

public class SelectColumns {

  // Properties

  private Select select;

  // Constructor

  SelectColumns(final SQLDialect sqlDialect, final Expression... queryColumns) {
    this.select = new Select(sqlDialect);
    this.select.setQueryColumns(Arrays.asList(queryColumns));
  }

  // Next stages

  public SelectFrom from(final TableOrView t) {
    return new SelectFrom(this.select, t);
  }

  // Execute

  public List<Row> execute() {
    return this.select.execute();
  }

}
