package explain.renderers;

import java.awt.Color;

public class Range {

  private double exp;
  private double minScalar;
  private double maxScalar;
  private Color minColor;
  private Color maxColor;

  private Double minValue;
  private Double maxValue;
  private double logExp;
  private double minlog;
  private double maxlog;

  public Range(final double exp, final double minScalar, final double maxScalar, final Color minColor,
      final Color maxColor) {
    if (minColor == null) {
      throw new IllegalArgumentException("minColor cannot be null.");
    }
    if (maxColor == null) {
      throw new IllegalArgumentException("maxColor cannot be null.");
    }
    this.exp = exp;
    this.logExp = Math.log(this.exp);
    this.minScalar = minScalar;
    this.maxScalar = maxScalar;
    this.minColor = minColor;
    this.maxColor = maxColor;
    this.minValue = null;
    this.maxValue = null;
  }

  public void register(final Double rows) {
    if (rows != null) {
      if (this.minValue == null || rows < this.minValue) {
        this.minValue = rows;
      }
      if (this.maxValue == null || rows > this.maxValue) {
        this.maxValue = rows;
      }
    }
  }

  public double computeScalar(final Double v) {
    if (this.minValue == null) {
      return this.minScalar;
    }
    if (v == null || v < this.minValue) {
      return minScalar;
    }
    if (v > this.maxValue) {
      return this.maxScalar;
    }
    if (this.maxValue - this.minValue < 0.001) {
      return this.minScalar;
    }
    this.minlog = Math.log(this.minValue) / this.logExp;
    this.maxlog = Math.log(this.maxValue) / this.logExp;
    double rlog = Math.log(v) / this.logExp;
    double ratio = (rlog - this.minlog) / (this.maxlog - this.minlog);
    double w = ratio * (this.maxScalar - this.minScalar) + this.minScalar;
    return w;
  }

  public Color computeColor(final Double v) {
    if (this.minValue == null) {
      return this.minColor;
    }
    if (v == null || v < this.minValue) {
      return minColor;
    }
    if (v > this.maxValue) {
      return this.maxColor;
    }
    if (this.maxValue - this.minValue < 0.001) {
      return this.minColor;
    }
    this.minlog = Math.log(this.minValue) / this.logExp;
    this.maxlog = Math.log(this.maxValue) / this.logExp;
    double rlog = Math.log(v) / this.logExp;
    double ratio = (rlog - this.minlog) / (this.maxlog - this.minlog);
    return mergeColor(this.minColor, this.maxColor, ratio);
  }

  public Color computeColorLinear(final Double v) {
    if (this.minValue == null) {
      return this.minColor;
    }
    if (v == null || v < this.minValue) {
      return minColor;
    }
    if (v > this.maxValue) {
      return this.maxColor;
    }
    if (this.maxValue - this.minValue < 0.001) {
      return this.minColor;
    }
    double ratio = (v - this.minValue) / (this.maxValue - this.minValue);
    return mergeColor(this.minColor, this.maxColor, ratio);
  }

  private Color mergeColor(final Color from, final Color to, final double ratio) {
    int r = mergeInt(from.getRed(), to.getRed(), ratio);
    int g = mergeInt(from.getGreen(), to.getGreen(), ratio);
    int b = mergeInt(from.getBlue(), to.getBlue(), ratio);
    return new Color(r, g, b);
  }

  private int mergeInt(final int from, final int to, final double ratio) {
    return (int) Math.round(ratio * (to - from) + from);

  }

}
