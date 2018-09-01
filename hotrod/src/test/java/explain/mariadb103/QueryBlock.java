package explain.mariadb103;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class QueryBlock {

  // Properties

  @SerializedName("select_id")
  private Integer selectId = null;

  @SerializedName("const_condition")
  private String constCondition = null;

  @SerializedName("table")
  private Table table = null;

  @SerializedName("block-nl-join")
  private BlockNlJoin blockNlJoin = null;

  @SerializedName("subqueries")
  private List<Subquery> subqueries = null;

  // @SerializedName("cost_info")
  // private CostInfo costInfo = null;
  //
  // @SerializedName("nested_loop")
  // private List<NestedLoopElement> nestedLoopElements = new
  // ArrayList<NestedLoopElement>();
  //
  // @SerializedName("optimized_away_subqueries")
  // private List<OptimizedAwaySubquery> optimizedAwaySubqueries = new
  // ArrayList<OptimizedAwaySubquery>();
  //
  // private String message;
  //
  // // Getters & Setters
  //
  // public Integer getSelectId() {
  // return selectId;
  // }
  //
  // public CostInfo getCostInfo() {
  // return costInfo;
  // }
  //
  // public Table getTable() {
  // return table;
  // }
  //
  // public List<NestedLoopElement> getNestedLoopElements() {
  // return nestedLoopElements;
  // }
  //
  // public List<OptimizedAwaySubquery> getOptimizedAwaySubqueries() {
  // return optimizedAwaySubqueries;
  // }
  //
  // public String getMessage() {
  // return message;
  // }
  //
  // // toString
  //
  // @Override
  // public String toString() {
  // return "QueryBlock [selectId=" + selectId + ", costInfo=" + costInfo + ",
  // table=" + table + ", nestedLoopElements="
  // + nestedLoopElements + "]";
  // }

}
