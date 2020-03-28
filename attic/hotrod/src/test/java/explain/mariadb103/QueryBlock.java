package explain.mariadb103;

import java.util.ArrayList;
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
  private List<Subquery> subqueries = new ArrayList<Subquery>();

  // Getters

  public Integer getSelectId() {
    return selectId;
  }

  public String getConstCondition() {
    return constCondition;
  }

  public Table getTable() {
    return table;
  }

  public BlockNlJoin getBlockNlJoin() {
    return blockNlJoin;
  }

  public List<Subquery> getSubqueries() {
    return subqueries;
  }

}
