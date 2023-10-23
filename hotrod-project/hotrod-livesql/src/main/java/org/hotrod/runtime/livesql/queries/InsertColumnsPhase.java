package org.hotrod.runtime.livesql.queries;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.Select;

public class InsertColumnsPhase {

  // Properties

  private LiveSQLContext context;
  private InsertObject insert;

  // Constructor

  public InsertColumnsPhase(final LiveSQLContext context, final InsertObject insert) {
    this.context = context;
    this.insert = insert;
  }

  // Next stages

  public InsertValuesPhase values(final List<Expression> values) {
    this.insert.setValues(values);
    return new InsertValuesPhase(this.context, this.insert);
  }

  public InsertSelectPhase select(final Select<?> select) {
    this.insert.setSelect(select.getCombinedSelect().getLastSelect());
    return new InsertSelectPhase(this.context, this.insert);
  }

}
