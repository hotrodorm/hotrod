package org.hotrod.dynamic.insert;

public class InsertProperties {

  private String sequencePreFetchSQL;
  private String primaryKeyParameterName;
  private String identityColumnName;

  public InsertProperties(String sequencePreFetchSQL, String primaryKeyParameterName, String identityColumnName) {
    this.sequencePreFetchSQL = sequencePreFetchSQL;
    this.primaryKeyParameterName = primaryKeyParameterName;
    this.identityColumnName = identityColumnName;
  }

  public String getSequencePreFetchSQL() {
    return sequencePreFetchSQL;
  }

  public String getPrimaryKeyParameterName() {
    return primaryKeyParameterName;
  }

  public String getIdentityColumnName() {
    return identityColumnName;
  }

}
