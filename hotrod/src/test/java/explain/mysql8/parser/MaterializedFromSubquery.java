package explain.mysql8.parser;

import com.google.gson.annotations.SerializedName;

public class MaterializedFromSubquery {

  @SerializedName("using_temporary_table")
  private Boolean usingTemporaryTable;

  @SerializedName("dependent")
  private Boolean dependent;

  @SerializedName("cacheable")
  private Boolean cacheable;

  @SerializedName("query_block")
  private QueryBlock queryBlock;

  public Boolean getUsingTemporaryTable() {
    return usingTemporaryTable;
  }

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
