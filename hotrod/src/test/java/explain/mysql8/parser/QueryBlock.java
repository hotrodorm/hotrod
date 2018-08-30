package explain.mysql8.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class QueryBlock {

  // Properties

  @SerializedName("select_id")
  private Integer selectId = null;

  @SerializedName("cost_info")
  private CostInfo costInfo = null;

  private Table table = null;

  @SerializedName("nested_loop")
  private List<NestedLoopElement> nestedLoopElements = new ArrayList<NestedLoopElement>();

  @SerializedName("optimized_away_subqueries")
  private List<OptimizedAwaySubquery> optimizedAwaySubqueries = new ArrayList<OptimizedAwaySubquery>();

  private String message;

  // Getters & Setters

  public Integer getSelectId() {
    return selectId;
  }

  public CostInfo getCostInfo() {
    return costInfo;
  }

  public Table getTable() {
    return table;
  }

  public List<NestedLoopElement> getNestedLoopElements() {
    return nestedLoopElements;
  }

  public List<OptimizedAwaySubquery> getOptimizedAwaySubqueries() {
    return optimizedAwaySubqueries;
  }

  public String getMessage() {
    return message;
  }

  // toString

  @Override
  public String toString() {
    return "QueryBlock [selectId=" + selectId + ", costInfo=" + costInfo + ", table=" + table + ", nestedLoopElements="
        + nestedLoopElements + "]";
  }

}
