package explain.mariadb103;

import com.google.gson.annotations.SerializedName;

public class Materialized {

  @SerializedName("unique")
  private Integer unique;

  @SerializedName("query_block")
  private QueryBlock queryBlock = null;

  // Getters

  public Integer getUnique() {
    return unique;
  }

  public QueryBlock getQueryBlock() {
    return queryBlock;
  }

}
