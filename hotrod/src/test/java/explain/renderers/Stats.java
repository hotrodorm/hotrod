package explain.renderers;

import java.awt.Color;

public class Stats {

  // Constants

  private static final double ROWS_EXP = 2.0;
  private static final double ROWS_MIN_WIDTH = 1.0;
  private static final double ROWS_MAX_WIDTH = 8.0;
  private static final Color ROWS_MIN_COLOR = new Color(150, 50, 50);
  private static final Color ROWS_MAX_COLOR = new Color(255, 0, 0);

  private static final double COST_EXP = 10.0;
  private static final Color COST_MIN_COLOR = new Color(255, 255, 255);
  private static final Color COST_MAX_COLOR = new Color(255, 70, 70);

  // Properties

  private Range rows;
  private Range cost;
  private Range time;

  // Constructor

  public Stats() {
    this.rows = new Range(ROWS_EXP, ROWS_MIN_WIDTH, ROWS_MAX_WIDTH, ROWS_MIN_COLOR, ROWS_MAX_COLOR);
    this.cost = new Range(COST_EXP, 1, 100, COST_MIN_COLOR, COST_MAX_COLOR);
    this.time = new Range(COST_EXP, 1, 100, COST_MIN_COLOR, COST_MAX_COLOR);
  }

  // Getters

  public Range getRows() {
    return rows;
  }

  public Range getCost() {
    return cost;
  }

  public Range getTime() {
    return time;
  }

  // Range class

  public static class Range {

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

    public void register(final Double v) {
      if (v != null) {
        if (this.minValue == null || v < this.minValue) {
          this.minValue = v;
        }
        if (this.maxValue == null || v > this.maxValue) {
          this.maxValue = v;
        }
      }
    }

    public Double getMinValue() {
      return minValue;
    }

    public Double getMaxValue() {
      return maxValue;
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

    public Color computeLogColor(final Double v) {
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

    public double computeLinearRatio(final Double v) {
      if (this.minValue == null) {
        return 0.0;
      }
      if (v == null || v < this.minValue) {
        return 0.0;
      }
      if (v > this.maxValue) {
        return 1.0;
      }
      if (this.maxValue - this.minValue < 0.001) {
        return 0.0;
      }
      double ratio = (v - this.minValue) / (this.maxValue - this.minValue);
      return ratio;
    }

    public Color computeLinearColor(final Double v) {
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

}
