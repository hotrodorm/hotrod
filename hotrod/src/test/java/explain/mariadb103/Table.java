package explain.mariadb103;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Table {

  @SerializedName("table_name")
  private String tableName = null;

  @SerializedName("access_type")
  private String accessType = null;

  @SerializedName("possible_keys")
  private List<String> possibleKeys = new ArrayList<String>();

  @SerializedName("key")
  private String key = null;

  @SerializedName("used_key_parts")
  private List<String> usedKeyParts = new ArrayList<String>();

  @SerializedName("key_length")
  private Integer keyLength = null;

  @SerializedName("ref")
  private List<String> ref = new ArrayList<String>();

  @SerializedName("rows")
  private Double rows = null;

  @SerializedName("filtered")
  private Double filtered = null;

  @SerializedName("materialized")
  private Materialized materialized = null;

  @SerializedName("attached_condition")
  private String attachedCondition = null;

  @SerializedName("message")
  private String message = null;

  // Getters

  public String getTableName() {
    return tableName;
  }

  public String getAccessType() {
    return accessType;
  }

  public List<String> getPossibleKeys() {
    return possibleKeys;
  }

  public String getKey() {
    return key;
  }

  public List<String> getUsedKeyParts() {
    return usedKeyParts;
  }

  public Integer getKeyLength() {
    return keyLength;
  }

  public List<String> getRef() {
    return ref;
  }

  public Double getRows() {
    return rows;
  }

  public Double getFiltered() {
    return filtered;
  }

  public Materialized getMaterialized() {
    return materialized;
  }

  public String getAttachedCondition() {
    return attachedCondition;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "Table [tableName=" + tableName + ", accessType=" + accessType + ", possibleKeys=" + possibleKeys + ", key="
        + key + ", usedKeyParts=" + usedKeyParts + ", keyLength=" + keyLength + ", ref=" + ref + ", rows=" + rows
        + ", filtered=" + filtered + ", materialized=" + materialized + ", attachedCondition=" + attachedCondition
        + ", message=" + message + "]";
  }

}
