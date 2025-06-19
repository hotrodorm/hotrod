package org.hotrod.livesql.queries;

import java.util.List;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;

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

  public InsertValuesPhase values(final List<ComparableExpression> values) {
    this.insert.setValues(values);
    return new InsertValuesPhase(this.context, this.insert);
  }

  public InsertSelectPhase select(final Select<?> select) {
    this.insert.setSelect(SShield.getCombinedSelect(select).getLastSelect());
    return new InsertSelectPhase(this.context, this.insert);
  }

}
