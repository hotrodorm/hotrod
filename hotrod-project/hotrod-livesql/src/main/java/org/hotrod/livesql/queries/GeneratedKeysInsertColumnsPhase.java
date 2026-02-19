package org.hotrod.livesql.queries;

import java.util.Arrays;
import java.util.logging.Logger;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.queries.select.sets.SelectObject;

public class GeneratedKeysInsertColumnsPhase<T> {

  private static final Logger log = Logger.getLogger(GeneratedKeysInsertColumnsPhase.class.getName());

  // Properties

  private LiveSQLContext context;
  private GeneratedKeysInsertObject<T> insert;

  // Constructor

  public GeneratedKeysInsertColumnsPhase(final LiveSQLContext context, final GeneratedKeysInsertObject<T> insert) {
    log.fine("init");
    this.context = context;
    this.insert = insert;
  }

  // Next stages

  public GeneratedKeysInsertValuesPhase<T> values(final ComparableExpression... values) {
    this.insert.setValues(Arrays.asList(values));
    return new GeneratedKeysInsertValuesPhase<>(this.context, this.insert);
  }

  public GeneratedKeysInsertSelectPhase<T> select(final Select<?> select) {
    SelectObject<?> s = SShield.getCombinedSelect(select);
    log.info("--- s=" + s);
    this.insert.setSelect(s);
    return new GeneratedKeysInsertSelectPhase<T>(this.context, this.insert);
  }

}
