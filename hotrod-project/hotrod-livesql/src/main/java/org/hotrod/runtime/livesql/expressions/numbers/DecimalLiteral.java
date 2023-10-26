package org.hotrod.runtime.livesql.expressions.numbers;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrodorm.hotrod.utils.SUtil;

public class DecimalLiteral extends NumericLiteral {

  private static final int MAX_DECIMAL_PLACES = 40;

  // Constructor

  public DecimalLiteral(final Number value, final int decimalPlaces) {
    super(Expression.PRECEDENCE_LITERAL);
    this.formatted = getFormatter(decimalPlaces).format(value);
  }

  public DecimalLiteral(final BigDecimal value, final int decimalPlaces) {
    super(Expression.PRECEDENCE_LITERAL);
    this.formatted = getFormatter(decimalPlaces).format(value);
  }

  // Utils

  private DecimalFormat getFormatter(final int decimalPlaces) {
    if (decimalPlaces < 0 || decimalPlaces > MAX_DECIMAL_PLACES) {
      throw new InvalidLiteralException("The number of decimal places for a numeric literal must be between 0 and "
          + MAX_DECIMAL_PLACES + ", but it was specified as " + decimalPlaces + ".");
    }
    DecimalFormat df = decimalPlaces == 0 ? new DecimalFormat("0")
        : new DecimalFormat("0." + SUtil.repeat('0', decimalPlaces));
    return df;
  }

}
