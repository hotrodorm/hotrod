package org.hotrod.livesql.queries;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;

public class GeneratedKeysInsertColumnsPhase<T> {

  // Properties

  private LiveSQLContext context;
  private GeneratedKeysInsertObject<T> insert;

  // Constructor

  public GeneratedKeysInsertColumnsPhase(final LiveSQLContext context, final GeneratedKeysInsertObject<T> insert) {
    this.context = context;
    this.insert = insert;
  }

  // Next stages

  public GeneratedKeysInsertValuesPhase<T> values(final ComparableExpression... values) {
    this.insert.setValues(Arrays.asList(values));
    return new GeneratedKeysInsertValuesPhase<>(this.context, this.insert);
  }

}
