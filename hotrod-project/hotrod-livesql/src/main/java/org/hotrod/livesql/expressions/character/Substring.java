package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.numeric.GeneralNumericExpression;
import org.hotrod.livesql.queries.QueryWriter;

public class Substring extends BuiltInCharFunction {

  private GeneralCharExpression string;
  private GeneralNumericExpression from;
  private GeneralNumericExpression length;

  public Substring(final GeneralCharExpression string, final GeneralNumericExpression from, final GeneralNumericExpression length) {
    super();
    this.string = string;
    this.from = from;
    this.length = length;
    super.register(this.string);
    super.register(this.from);
    super.register(this.length);
  }

  public Substring(final GeneralCharExpression string, final GeneralNumericExpression from) {
    super();
    this.string = string;
    this.from = from;
    this.length = null;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    w.getSQLDialect().getFunctionRenderer().substr(w, this.string, this.from, this.length);
  }

}
