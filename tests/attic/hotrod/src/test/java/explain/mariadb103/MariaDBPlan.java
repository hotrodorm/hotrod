package explain.mariadb103;

import com.google.gson.annotations.SerializedName;

public class MariaDBPlan {

  @SerializedName("query_block")
  private QueryBlock queryBlock = null;

  // Getters & Setters

  public QueryBlock getQueryBlock() {
    return queryBlock;
  }

  public String toString() {
    return "queryBlock=" + this.queryBlock;
  }

}
