package org.hotrod.dynamic.insert;

public class InsertProperties {

  private String sequencePreFetchSQL;
  private String pkName;

  public InsertProperties(String sequencePreFetchSQL, String pkName) {
    this.sequencePreFetchSQL = sequencePreFetchSQL;
    this.pkName = pkName;
  }

  public String getSequencePreFetchSQL() {
    return sequencePreFetchSQL;
  }

  public String getPrimaryKeyName() {
    return pkName;
  }

}
