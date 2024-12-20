package org.hotrod.generator.jdbc;

import org.hotrod.dynamic.insert.PrimaryKeyRetrievalMode;

public class InsertMechanics {

  private PrimaryKeyRetrievalMode mode;
  private String sequencePreFetchSQL;
  private String primaryKeyParameterName;
  private String sequenceInlineSQL;
  private String[] generatedKeysNames;

  public InsertMechanics(PrimaryKeyRetrievalMode mode) {
    this.mode = mode;
    this.sequencePreFetchSQL = null;
    this.primaryKeyParameterName = null;
    this.sequenceInlineSQL = null;
    this.generatedKeysNames = null;
  }

  public InsertMechanics(PrimaryKeyRetrievalMode mode, String sequencePreFetchSQL, String primaryKeyParameterName,
      String sequenceInlineSQL, String... generatedKeysNames) {
    this.mode = mode;
    this.sequencePreFetchSQL = sequencePreFetchSQL;
    this.primaryKeyParameterName = primaryKeyParameterName;
    this.sequenceInlineSQL = sequenceInlineSQL;
    this.generatedKeysNames = generatedKeysNames;
  }

  public PrimaryKeyRetrievalMode getMode() {
    return mode;
  }

  public String getSequencePreFetchSQL() {
    return sequencePreFetchSQL;
  }

  public String getPrimaryKeyParameterName() {
    return primaryKeyParameterName;
  }

  public String getSequenceInlineSQL() {
    return this.sequenceInlineSQL;
  }

  public String[] getGeneratedKeysNames() {
    return generatedKeysNames;
  }

}
