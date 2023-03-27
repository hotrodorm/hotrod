package org.hotrod.runtime.livesql.queries;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;

public class InsertColumnsPhase {

  // Properties

  private Insert insert;

  // Constructor

  public InsertColumnsPhase(final Insert insert) {
    this.insert = insert;
  }

  // Next stages

  public InsertValuesPhase values(final List<Expression> values) {
    this.insert.setValues(values);
    return new InsertValuesPhase(this.insert);
  }

  public InsertSelectPhase select(final ExecutableSelect<?> select) {
    this.insert.setSelect(select);
    return new InsertSelectPhase(this.insert);
  }

}
