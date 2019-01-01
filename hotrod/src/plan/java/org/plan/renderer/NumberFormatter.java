package org.plan.renderer;

import java.text.DecimalFormat;

public class NumberFormatter {

  private double multiplier;
  private DecimalFormat formatter;

  public NumberFormatter(final double multiplier, final DecimalFormat formatter) {
    this.multiplier = multiplier;
    this.formatter = formatter;
    if (this.formatter == null) {
      throw new IllegalArgumentException("formatter cannot be null");
    }
  }

  public String format(final Double n) {
    return n == null ? null : this.formatter.format(n * this.multiplier);
  }

}
