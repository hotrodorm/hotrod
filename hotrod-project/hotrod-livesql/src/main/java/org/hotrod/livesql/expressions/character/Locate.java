package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Locate extends NumericExpression {

  private GeneralCharExpression substring;
  private GeneralCharExpression string;
  private GeneralNumericExpression from;

  public Locate(final GeneralCharExpression substring, final GeneralCharExpression string, final GeneralNumericExpression from) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.substring = substring;
    this.string = string;
    this.from = from;
    super.register(this.substring);
    super.register(this.string);
    super.register(this.from);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().locate(w, this.substring, this.string, this.from);
  }

}
