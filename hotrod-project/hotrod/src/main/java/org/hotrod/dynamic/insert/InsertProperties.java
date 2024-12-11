package org.hotrod.dynamic.insert;

public class InsertProperties {

  private String sequencePreFetchSQL;
  private String primaryKeyParameterName;
  private String[] generatedKeysNames;

  public InsertProperties(String sequencePreFetchSQL, String primaryKeyParameterName, String... generatedKeysNames) {
    this.sequencePreFetchSQL = sequencePreFetchSQL;
    this.primaryKeyParameterName = primaryKeyParameterName;
    this.generatedKeysNames = generatedKeysNames;
  }

  public String getSequencePreFetchSQL() {
    return sequencePreFetchSQL;
  }

  public String getPrimaryKeyParameterName() {
    return primaryKeyParameterName;
  }

  public String[] getGeneratedKeysNames() {
    return generatedKeysNames;
  }

}
