package org.hotrod.runtime.livesql.expressions.predicates.converter;

import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;

public class ConvertedIn<R, D> extends Predicate {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ConvertedIn.class.getName());

  private ConvertedColumn<R, D> c;
  private TypeConverter<R, D> converter;
  private D[] d;

  public ConvertedIn(final ConvertedColumn<R, D> c, final TypeConverter<R, D> converter,
      @SuppressWarnings("unchecked") final D... d) {
    super(Expression.PRECEDENCE_EQ_NE_LT_LE_GT_GE);
    this.c = c;
    this.converter = converter;
    if (d.length == 0) {
      throw new RuntimeException("The IN operator must use a non-empty list of values, but none was provided.");
    }
    this.d = d;
  }

  @Override
  protected void renderTo(QueryWriter w) {

    // 1. The column

    super.renderInner(this.c, w);

    // 2. Operator open

    w.write(" IN (");

    // 3. The encoded value

    boolean first = true;
    for (D v : this.d) {
      R raw;
      try {
        raw = this.converter.encode(v, null);
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
