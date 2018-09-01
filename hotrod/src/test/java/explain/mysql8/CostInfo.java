package explain.mysql8;

import com.google.gson.annotations.SerializedName;

public class CostInfo {

  // "query_cost": "0.35"
  // "read_cost": "0.25",
  // "eval_cost": "0.10",
  // "prefix_cost": "0.35",
  // "data_read_per_join": "424"

  @SerializedName("query_cost")
  private Double queryCost = null;

  @SerializedName("read_cost")
  private Double readCost = null;

  @SerializedName("eval_cost")
  private Double evalCost = null;

  @SerializedName("prefix_cost")
  private Double prefixCost = null;

  @SerializedName("data_read_per_join")
  private Double dataReadPerJoin = null;

  // Behavior

  public Double computeCost() {
    if (this.queryCost != null) {
      return this.queryCost;
    } else {
      Double cost = null;
      cost = add(cost, this.readCost);
      cost = add(cost, this.evalCost);
      cost = add(cost, this.prefixCost);
      return cost;
    }
  }

  private Double add(final Double a, final Double b) {
    if (a == null) {
      return b;
    } else {
      if (b == null) {
        return a;
      } else {
        return a + b;
      }
    }
  }

  // Getters & Setters

  public Double getQueryCost() {
    return queryCost;
  }

  public Double getReadCost() {
    return readCost;
  }

  public Double getEvalCost() {
    return evalCost;
  }

  public Double getPrefixCost() {
    return prefixCost;
  }

  public Double getDataReadPerJoin() {
    return dataReadPerJoin;
  }

  @Override
  public String toString() {
    return "CostInfo [queryCost=" + queryCost + ", readCost=" + readCost + ", evalCost=" + evalCost + ", prefixCost="
        + prefixCost + ", dataReadPerJoin=" + dataReadPerJoin + "]";
  }

}
