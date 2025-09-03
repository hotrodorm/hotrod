package org.hotrod.livesql.expressions.bool.converter;

import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.SortableExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ConvertedCoalesce<R, D> extends SortableExpression {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ConvertedCoalesce.class.getName());

  private ConvertedColumn<R, D> c;
  private TypeConverter<R, D> converter;
  private D d;

  public ConvertedCoalesce(final ConvertedColumn<R, D> c, final TypeConverter<R, D> converter, final D d) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.c = c;
    this.converter = converter;
    this.d = d;
  }

  @Override
  protected void renderTo(QueryWriter w) {

    w.write("coalesce(");

    super.renderInner(this.c, w);

    w.write(", ");

    R raw = this.converter.encode(this.d, null);
    RenderedParameter rp = w.registerParameter(raw);
    w.write(rp.getPlaceholder());

    w.write(")");

  }

}
