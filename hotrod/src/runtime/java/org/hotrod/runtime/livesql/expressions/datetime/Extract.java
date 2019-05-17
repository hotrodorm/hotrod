package org.hotrod.runtime.livesql.expressions.datetime;

import java.util.Date;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.numbers.NumericFunction;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class Extract extends NumericFunction {

  private Expression<Date> datetime;
  private DateTimeFieldExpression field;

  public Extract(final Expression<Date> datetime, final DateTimeFieldExpression field) {
    super();
    this.datetime = datetime;
    this.field = field;
  }

  @Override
  public void renderTo(final QueryWriter w) {
    w.getSqlDialect().getFunctionRenderer().extract(w, this.datetime, this.field);
  }

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.datetime.validateTableReferences(tableReferences, ag);
    this.field.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.datetime.designateAliases(ag);
    this.field.designateAliases(ag);
  }

}
