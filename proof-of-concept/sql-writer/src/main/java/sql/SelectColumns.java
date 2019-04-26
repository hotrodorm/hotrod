package sql;

import java.util.Arrays;
import java.util.List;

import com.sun.rowset.internal.Row;

import metadata.Column;
import metadata.TableOrView;

public class SelectColumns {

  // Properties

  private Select select;

  // Constructor

  SelectColumns(final SQLDialect sqlTranslator, final Column... columns) {
    this.select = new Select(sqlTranslator);
    this.select.setColumns(Arrays.asList(columns));
  }

  // Next stages

  public SelectFrom from(final TableOrView t) {
    return new SelectFrom(this.select, t);
  }

  // Execute

  public List<Row> execute() {
    return null;
  }

}
