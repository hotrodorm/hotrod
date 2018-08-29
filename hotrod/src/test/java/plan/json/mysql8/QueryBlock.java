package plan.json.mysql8;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class QueryBlock {

  // Properties

  @SerializedName("select_id")
  private String selectId = null;

  @SerializedName("cost_info")
  private CostInfo costInfo = null;

  private Table table = null;

  private List<NestedLoopElement> nestedLoopElements = new ArrayList<NestedLoopElement>();

  // Getters & Setters

  public String getSelectId() {
    return selectId;
  }

  public void setSelectId(String selectId) {
    this.selectId = selectId;
  }

  public CostInfo getCostInfo() {
    return costInfo;
  }

  public void setCostInfo(CostInfo costInfo) {
    this.costInfo = costInfo;
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public List<NestedLoopElement> getNestedLoopElements() {
    return nestedLoopElements;
  }

  public void setNestedLoopElements(List<NestedLoopElement> nestedLoopElements) {
    this.nestedLoopElements = nestedLoopElements;
  }

  // toString

  @Override
  public String toString() {
    return "QueryBlock [selectId=" + selectId + ", costInfo=" + costInfo + ", table=" + table + ", nestedLoopElements="
        + nestedLoopElements + "]";
  }

}
