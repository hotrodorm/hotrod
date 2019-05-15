package org.hotrod.runtime.livesql.expressions.datetime;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferencesValidator;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;

public class DateTimeFieldExpression extends Expression<DateTimeField> {

  private DateTimeField field;

  public DateTimeFieldExpression(final DateTimeField field) {
    super(Expression.PRECEDENCE_LITERAL);
    this.field = field;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.write(this.field.name().toLowerCase());
  }

  public static enum DateTimeField {
    YEAR, //
    MONTH, //
    DAY, //
    HOUR, //
    MINUTE, //
    SECOND, //
    TIMEZONE_HOUR, //
    TIMEZONE_MINUTE, //
    TZOFFSET, //
    QUARTER, //
    WEEK, //
    DOW, //
    MILLISECOND;
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferencesValidator tableReferences, final AliasGenerator ag) {
    // nothing to do
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    // nothing to do
  }

}
