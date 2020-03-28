package explain.mysql8;

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

  @SerializedName("rows_examined_per_scan")
  private Double rowsExaminedPerScan = null;

  @SerializedName("rows_produced_per_join")
  private Double rowsProducedPerJoin = null;

  @SerializedName("filtered")
  private Double filtered = null;

  @SerializedName("materialized_from_subquery")
  private MaterializedFromSubquery materializedFromSubquery;

  @Override
  public String toString() {
    return "Table [" + (tableName != null ? "tableName=" + tableName + ", " : "")
        + (accessType != null ? "accessType=" + accessType + ", " : "")
        + (possibleKeys != null ? "possibleKeys=" + possibleKeys + ", " : "") + (key != null ? "key=" + key + ", " : "")
        + (usedKeyParts != null ? "usedKeyParts=" + usedKeyParts + ", " : "")
        + (keyLength != null ? "keyLength=" + keyLength + ", " : "") + (ref != null ? "ref=" + ref + ", " : "")
        + (rowsExaminedPerScan != null ? "rowsExaminedPerScan=" + rowsExaminedPerScan + ", " : "")
        + (rowsProducedPerJoin != null ? "rowsProducedPerJoin=" + rowsProducedPerJoin + ", " : "")
        + (filtered != null ? "filtered=" + filtered + ", " : "")
        + (costInfo != null ? "costInfo=" + costInfo + ", " : "")
        + (usedColumns != null ? "usedColumns=" + usedColumns + ", " : "")
        + (attachedCondition != null ? "attachedCondition=" + attachedCondition + ", " : "")
        + (attachedSubqueries != null ? "attachedSubqueries=" + attachedSubqueries : "") + "]";
  }

  @SerializedName("cost_info")
  private CostInfo costInfo = null;

  @SerializedName("used_columns")
  private List<String> usedColumns = new ArrayList<String>();

  @SerializedName("attached_condition")
  private String attachedCondition = null;

  @SerializedName("attached_subqueries")
  private List<AttachedSubquery> attachedSubqueries = new ArrayList<AttachedSubquery>();

  // Getters / Setters

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

  public Double getRowsExaminedPerScan() {
    return rowsExaminedPerScan;
  }

  public Double getRowsProducedPerJoin() {
    return rowsProducedPerJoin;
  }

  public Double getFiltered() {
    return filtered;
  }

  public CostInfo getCostInfo() {
    return costInfo;
  }

  public List<String> getUsedColumns() {
    return usedColumns;
  }

  public String getAttachedCondition() {
    return attachedCondition;
  }

  public List<AttachedSubquery> getAttachedSubqueries() {
    return attachedSubqueries;
  }

  public MaterializedFromSubquery getMaterializedFromSubquery() {
    return materializedFromSubquery;
  }

}
