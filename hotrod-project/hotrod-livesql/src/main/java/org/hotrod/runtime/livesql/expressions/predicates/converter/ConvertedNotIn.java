package org.hotrod.runtime.livesql.expressions.predicates.converter;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ConvertedNotIn<R, D> extends Predicate {

  private static final Logger log = Logger.getLogger(ConvertedNotIn.class.getName());

  private ConvertedColumn<R, D> a;
  private D[] d;
  private TypeConverter<R, D> converter;

  public ConvertedNotIn(final ConvertedColumn<R, D> a, final TypeConverter<R, D> converter, final D... d) {
    super(Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
    this.a = a;
    this.converter = converter;

    if (d.length == 0) {
      throw new RuntimeException("The NOT IN operator must use a non-empty list of values, but none was provided.");
    }

    this.d = d;
    super.register(this.a);
  }

  @Override
  protected void renderTo(QueryWriter w) {

    // 1. The column

    super.renderInner(this.a, w);

    // 2. Operator open

    w.write(" NOT IN (");

    // 3. The encoded value

    boolean first = true;
    for (D v : this.d) {
      R raw;
      try {
        raw = this.converter.encode(v, null);
//        log.info("raw=" + raw);
      } catch (SQLException e) {
        throw new RuntimeException("Could not encode converted value to raw value.", e);
      }
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      RenderedParameter rp = w.registerParameter(raw);
      w.write(rp.getPlaceholder());
    }

    // 3. Operator close

    w.write(")");

  }

}
