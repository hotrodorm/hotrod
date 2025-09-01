package org.plan.renderer;

import java.awt.Color;

public class PlanStats {

  // Properties

  private Range rows;
  private Range cost;
  private Range time;

  // Constructor

  public PlanStats() {
    this.rows = new Range();
    this.cost = new Range();
    this.time = new Range();
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

    private Double minValue;
    private Double maxValue;

    public Range() {
      this.minValue = null;
      this.maxValue = null;
    }

    public void addSample(final Double v) {
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

    public ScalarFunction getLogScalarFunction(final double minScalar, final double maxScalar, final double exp) {
      return new LogScalarFunction(minScalar, maxScalar, exp, this.minValue, this.maxValue);
    }

    public ScalarFunction getRatioScalarFunction() {
      return new RatioScalarFunction(this.minValue, this.maxValue);
    }

    public ColorFunction getLogColorFunction(final Color minColor, final Color maxColor, final double exp) {
      return new LogColorFunction(minColor, maxColor, exp, this.minValue, this.maxValue);
    }

    public ColorFunction getLinearColorFunction(final Color minColor, final Color maxColor) {
      return new LinearColorFunction(minColor, maxColor, this.minValue, this.maxValue);
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("minValue=" + minValue + "\n");
      sb.append("maxValue=" + maxValue + "\n");
      return sb.toString();
    }

  }

  // Functions

  public interface ScalarFunction {
    double apply(Double value);
  }

  public static class LogScalarFunction implements ScalarFunction {

    private double minScalar;
    private double maxScalar;

    private Double minValue;
    private Double maxValue;

    private double minlog;
    private double maxlog;
    private double logExp;

    public LogScalarFunction(final double minScalar, final double maxScalar, final double exp, final Double minValue,
        final Double maxValue) {
      this.minScalar = minScalar;
      this.maxScalar = maxScalar;
      this.minValue = minValue;
      this.maxValue = maxValue;

      this.logExp = Math.log(exp);
      if (this.minValue == null || this.maxValue == null) {
        this.minlog = 0;
        this.maxlog = 0;
      } else {
        this.minlog = Math.log(this.minValue) / this.logExp;
        this.maxlog = Math.log(this.maxValue) / this.logExp;
      }

    }

    @Override
    public double apply(final Double value) {
      if (this.minValue == null || this.maxValue == null) {
        return minScalar;
      }
      if (value == null || value < this.minValue) {
        return minScalar;
      }
      if (value > this.maxValue) {
        return maxScalar;
      }
      if (this.maxValue - this.minValue < 0.001) {
        return minScalar;
      }
      double rlog = Math.log(value) / this.logExp;
      double ratio = (rlog - this.minlog) / (this.maxlog - this.minlog);
      double w = ratio * (this.maxScalar - this.minScalar) + this.minScalar;
      return w;
    }

  }

  public static class RatioScalarFunction implements ScalarFunction {

    private Double minValue;
    private Double maxValue;

    public RatioScalarFunction(final Double minValue, final Double maxValue) {
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    @Override
    public double apply(final Double value) {
      if (this.minValue == null) {
        return 0.0;
      }
      if (value == null || value < this.minValue) {
        return 0.0;
      }
      if (value > this.maxValue) {
        return 1.0;
      }
      if (this.maxValue - this.minValue < 0.001) {
        return 0.0;
      }
      double ratio = (value - this.minValue) / (this.maxValue - this.minValue);
      return ratio;
    }

  }

  public interface ColorFunction {
    Color apply(Double value);
  }

  public static class LogColorFunction implements ColorFunction {

    private Color minColor;
    private Color maxColor;

    private Double minValue;
    private Double maxValue;

    private double minlog;
    private double maxlog;
    private double logExp;

    public LogColorFunction(final Color minColor, final Color maxColor, final double exp, final Double minValue,
        final Double maxValue) {
      this.minColor = minColor;
      this.maxColor = maxColor;
      this.minValue = minValue;
      this.maxValue = maxValue;

      this.logExp = Math.log(exp);
      if (this.minValue == null || this.maxValue == null) {
        this.minlog = 0;
        this.maxlog = 0;
      } else {
        this.minlog = Math.log(this.minValue) / this.logExp;
        this.maxlog = Math.log(this.maxValue) / this.logExp;
      }

    }

    @Override
    public Color apply(final Double v) {
      if (this.minValue == null) {
        return this.minColor;
      }
      if (v == null || v < this.minValue) {
        return this.minColor;
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
      return PlanStats.interpolateColor(this.minColor, this.maxColor, ratio);
    }

  }

  public static class LinearColorFunction implements ColorFunction {

    private Color minColor;
    private Color maxColor;

    private Double minValue;
    private Double maxValue;

    public LinearColorFunction(final Color minColor, final Color maxColor, final Double minValue,
        final Double maxValue) {
      this.minColor = minColor;
      this.maxColor = maxColor;
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    @Override
    public Color apply(final Double v) {
      if (this.minValue == null) {
        return this.minColor;
      }
      if (v == null || v < this.minValue) {
        return this.minColor;
      }
      if (v > this.maxValue) {
        return this.maxColor;
      }
      if (this.maxValue - this.minValue < 0.001) {
        return this.minColor;
      }
      double ratio = (v - this.minValue) / (this.maxValue - this.minValue);
      return PlanStats.interpolateColor(this.minColor, this.maxColor, ratio);
    }

  }

  // Utilities

  public static Color interpolateColor(final Color from, final Color to, final double ratio) {
    int r = interpolateInt(from.getRed(), to.getRed(), ratio);
    int g = interpolateInt(from.getGreen(), to.getGreen(), ratio);
    int b = interpolateInt(from.getBlue(), to.getBlue(), ratio);
    return new Color(r, g, b);
  }

  private static int interpolateInt(final int from, final int to, final double ratio) {
    return (int) Math.round(ratio * (to - from) + from);
  }

}
