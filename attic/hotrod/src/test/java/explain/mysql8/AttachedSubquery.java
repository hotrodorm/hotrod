package explain.mysql8;

import com.google.gson.annotations.SerializedName;

public class AttachedSubquery {

  private Boolean dependent = null;

  private Boolean cacheable = null;

  private Table table = null;

  @SerializedName("query_block")
  private QueryBlock queryBlock = null;

  // Getters / Setters

  public Boolean getDependent() {
    return dependent;
  }

  public Boolean getCacheable() {
    return cacheable;
  }

  public Table getTable() {
    return table;
  }

  public QueryBlock getQueryBlock() {
    return queryBlock;
  }

}
