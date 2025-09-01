package explain.mysql8;

import com.google.gson.annotations.SerializedName;

public class OptimizedAwaySubquery {

  private Boolean dependent;
  private Boolean cacheable;

  @SerializedName("query_block")
  private QueryBlock queryBlock = null;

  public Boolean getDependent() {
    return dependent;
  }

  public Boolean getCacheable() {
    return cacheable;
  }

  public QueryBlock getQueryBlock() {
    return queryBlock;
  }

}
