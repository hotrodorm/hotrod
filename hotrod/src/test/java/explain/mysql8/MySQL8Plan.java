package explain.mysql8;

import com.google.gson.annotations.SerializedName;

public class MySQL8Plan {

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
