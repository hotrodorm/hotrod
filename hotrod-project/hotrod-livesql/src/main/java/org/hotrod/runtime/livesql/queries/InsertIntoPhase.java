package org.hotrod.runtime.livesql.queries;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.Select;

public class InsertIntoPhase {

  // Properties

  private LiveSQLContext context;
  private InsertObject insert;

  // Constructor

  public InsertIntoPhase(final LiveSQLContext context, final TableOrView into) {
    this.context = context;
    this.insert = new InsertObject();
    this.insert.setInto(into);
  }

  // Next stages

  public InsertColumnsPhase columns(final List<Column> columns) {
    this.insert.setColumns(columns);
    return new InsertColumnsPhase(this.context, this.insert);
  }

  public InsertValuesPhase values(final Expression... values) {
    this.insert.setValues(Arrays.asList(values));
    return new InsertValuesPhase(this.context, this.insert);
  }

  public InsertSelectPhase select(final Select<?> select) {
    this.insert.setSelect(select.getSelect());
    return new InsertSelectPhase(this.context, this.insert);
  }

}
