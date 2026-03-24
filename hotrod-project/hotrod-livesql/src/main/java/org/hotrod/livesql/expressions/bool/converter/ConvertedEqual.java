package org.hotrod.livesql.expressions.bool.converter;

import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ConvertedEqual<R, D> extends BooleanSyntaxExpression {

  private static final Logger log = Logger.getLogger(ConvertedEqual.class.getName());

  private ConvertedEntityColumn<R, D> c;
  private TypeConverter<R, D> converter;
  private D d;

  public ConvertedEqual(final ConvertedEntityColumn<R, D> c, final TypeConverter<R, D> converter, final D d) {
    super(Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
    log.fine("init");
    this.c = c;
    this.converter = converter;
    this.d = d;
  }

  @Override
  protected void renderTo(QueryWriter w) {

    // 1. The column

    this.c.renderTo(w);

    // 2. The operator

    w.write(" = ");

    // 3. The encoded value

    R raw = this.converter.encode(this.d, null);
    RenderedParameter rp = w.registerParameter(raw);
    w.write(rp.getPlaceholder());

  }

}
