package org.plan.renderer;

import java.awt.Color;

public class Stats {

  // Constants

  private static final double ROWS_EXP = 2.0;
  private static final double COST_EXP = 10.0;

  // Properties

  private Range rows;
  private Range cost;
  private Range time;

  // Constructor

  public Stats() {
    this.rows = new Range(ROWS_EXP);
    this.cost = new Range(COST_EXP);
    this.time = new Range(COST_EXP);
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

    private Double minValue;
    private Double maxValue;
    private double logExp;
    private double minlog;
    private double maxlog;

    public Range(final double exp) {
      this.exp = exp;
      this.logExp = Math.log(this.exp);
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

    public double computeLogScalar(final double minScalar, final double maxScalar, final Double v) {
      if (this.minValue == null) {
        return minScalar;
      }
      if (v == null || v < this.minValue) {
        return minScalar;
      }
      if (v > this.maxValue) {
        return maxScalar;
      }
      if (this.maxValue - this.minValue < 0.001) {
        return minScalar;
      }
      this.minlog = Math.log(this.minValue) / this.logExp;
      this.maxlog = Math.log(this.maxValue) / this.logExp;
      double rlog = Math.log(v) / this.logExp;
      double ratio = (rlog - this.minlog) / (this.maxlog - this.minlog);
      double w = ratio * (maxScalar - minScalar) + minScalar;
      return w;
    }

    public Color computeLogColor(final Color minColor, final Color maxColor, final Double v) {
      if (this.minValue == null) {
        return minColor;
      }
      if (v == null || v < this.minValue) {
        return minColor;
      }
      if (v > this.maxValue) {
        return maxColor;
      }
      if (this.maxValue - this.minValue < 0.001) {
        return minColor;
      }
      this.minlog = Math.log(this.minValue) / this.logExp;
      this.maxlog = Math.log(this.maxValue) / this.logExp;
      double rlog = Math.log(v) / this.logExp;
      double ratio = (rlog - this.minlog) / (this.maxlog - this.minlog);
      return mergeColor(minColor, maxColor, ratio);
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

    public Color computeLinearColor(final Color minColor, final Color maxColor, final Double v) {
      if (this.minValue == null) {
        return minColor;
      }
      if (v == null || v < this.minValue) {
        return minColor;
      }
      if (v > this.maxValue) {
        return maxColor;
      }
      if (this.maxValue - this.minValue < 0.001) {
        return minColor;
      }
      double ratio = (v - this.minValue) / (this.maxValue - this.minValue);
      return mergeColor(minColor, maxColor, ratio);
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

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("exp=" + exp + "\n");
      sb.append("minValue=" + minValue + "\n");
      sb.append("maxValue=" + maxValue + "\n");
      sb.append("logExp=" + logExp + "\n");
      sb.append("minlog=" + minlog + "\n");
      sb.append("maxlog=" + maxlog + "\n");
      return sb.toString();
    }

  }

}
