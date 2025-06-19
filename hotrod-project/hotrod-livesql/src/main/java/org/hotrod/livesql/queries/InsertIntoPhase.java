package org.hotrod.livesql.queries;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;

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

  public InsertColumnsPhase columns(final List<EntityColumn> columns) {
    this.insert.setColumns(columns);
    return new InsertColumnsPhase(this.context, this.insert);
  }

  public InsertValuesPhase values(final ComparableExpression... values) {
    this.insert.setValues(Arrays.asList(values));
    return new InsertValuesPhase(this.context, this.insert);
  }

  public InsertSelectPhase select(final Select<?> select) {
    this.insert.setSelect(SShield.getCombinedSelect(select).getLastSelect());
    return new InsertSelectPhase(this.context, this.insert);
  }

}
