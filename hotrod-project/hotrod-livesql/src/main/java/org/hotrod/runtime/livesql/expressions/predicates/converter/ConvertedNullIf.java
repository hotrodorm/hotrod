package org.hotrod.runtime.livesql.expressions.predicates.converter;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.SortableExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ConvertedNullIf<R, D> extends SortableExpression {

  private static final Logger log = Logger.getLogger(ConvertedNullIf.class.getName());

  private ConvertedColumn<R, D> c;
  private TypeConverter<R, D> converter;
  private D d;

  public ConvertedNullIf(final ConvertedColumn<R, D> c, final TypeConverter<R, D> converter, final D d) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.c = c;
    this.converter = converter;
    this.d = d;
    super.register(this.c);
  }

  @Override
  protected void renderTo(QueryWriter w) {

    w.write("nullif(");

    super.renderInner(this.c, w);

    w.write(", ");

    R raw;
    try {
      raw = this.converter.encode(this.d, null);
      log.info("raw=" + raw);
    } catch (SQLException e) {
      throw new RuntimeException("Could not encode converted value to raw value.", e);
    }
    RenderedParameter rp = w.registerParameter(raw);
    w.write(rp.getPlaceholder());

    w.write(")");

  }

}
