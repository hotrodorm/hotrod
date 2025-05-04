package org.hotrod.runtime.livesql.expressions.predicates.converter;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ConvertedEqual<R, D> extends Predicate {

  private static final Logger log = Logger.getLogger(ConvertedEqual.class.getName());

  private ConvertedColumn<R, D> a;
  private D b;
  private TypeConverter<R, D> converter;

  public ConvertedEqual(final ConvertedColumn<R, D> a, final D b, final TypeConverter<R, D> converter) {
    super(Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
    this.a = a;
    this.b = b;
    this.converter = converter;
    super.register(this.a);
  }

  @Override
  protected void renderTo(QueryWriter w) {

    // 1. The column

    super.renderInner(this.a, w);

    // 2. The operator

    w.write(" = ");

    // 3. The encoded value

    R raw;
    try {
      raw = this.converter.encode(this.b, null);
      log.info("raw=" + raw);
    } catch (SQLException e) {
      throw new RuntimeException("Could not encode converted value to raw value.", e);
    }
    RenderedParameter rp = w.registerParameter(raw);
    w.write(rp.getPlaceholder());

  }

}
