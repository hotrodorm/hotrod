package explain.mariadb103;

import com.google.gson.annotations.SerializedName;

public class BlockNlJoin {

  @SerializedName("table")
  private Table table = null;

  @SerializedName("buffer_type")
  private String bufferType = null;

  @SerializedName("buffer_size")
  private String bufferSize = null;

  @SerializedName("join_type")
  private String joinType = null;

  @SerializedName("attached_condition")
  private String attachedCondition = null;

  // Getters

  public Table getTable() {
    return table;
  }

  public String getBufferType() {
    return bufferType;
  }

  public String getBufferSize() {
    return bufferSize;
  }

  public String getJoinType() {
    return joinType;
  }

  public String getAttachedCondition() {
    return attachedCondition;
  }

}
