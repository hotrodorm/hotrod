package org.hotrod.livesql.expressions.datetime;

import java.time.temporal.Temporal;
import java.util.Date;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.SQLParameterWriter.RenderedParameter;

public class DateTimeConstant extends DateTimeExpression {

  // Properties

  private Date dvalue;
  private Temporal tvalue;
  private boolean isTemporal;
  private boolean parameterize;

  // Constructor

  public DateTimeConstant(final Date value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.parameterize = true;
    this.isTemporal = false;
    this.dvalue = value;
    this.tvalue = null;
  }

  public DateTimeConstant(final Temporal value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.parameterize = true;
    this.isTemporal = true;
    this.dvalue = null;
    this.tvalue = value;
  }

  // Rendering

  @Override
  protected void renderTo(final QueryWriter w) {
    if (this.isTemporal) {
      if (this.parameterize) {
        RenderedParameter p = w.registerParameter(this.tvalue);
        w.write(p.getPlaceholder());
      } else {
        w.write("" + this.tvalue);
      }
    } else {
      if (this.parameterize) {
        RenderedParameter p = w.registerParameter(this.dvalue);
        w.write(p.getPlaceholder());
      } else {
        w.write("" + this.dvalue);
      }
    }
  }

}
