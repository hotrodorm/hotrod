package org.hotrod.generator.jdbc;

import java.util.Arrays;

import org.hotrod.dynamic.insert.PrimaryKeyRetrievalMode;

public class InsertMechanics {

  private PrimaryKeyRetrievalMode mode;
  private String sequencePreFetchSQL;
  private String primaryKeyMemberName;
  private String sequenceInlineSQL;
  private String outputClause;
  private String[] generatedKeysNames;

  public InsertMechanics(PrimaryKeyRetrievalMode mode) {
    this.mode = mode;
    this.sequencePreFetchSQL = null;
    this.primaryKeyMemberName = null;
    this.sequenceInlineSQL = null;
    this.generatedKeysNames = null;
  }

  public InsertMechanics(PrimaryKeyRetrievalMode mode, String sequencePreFetchSQL, String primaryKeyMemberName,
      String sequenceInlineSQL, String outputClause, String[] generatedKeysNames) {
    this.mode = mode;
    this.sequencePreFetchSQL = sequencePreFetchSQL;
    this.primaryKeyMemberName = primaryKeyMemberName;
    this.sequenceInlineSQL = sequenceInlineSQL;
    this.outputClause = outputClause;
    this.generatedKeysNames = generatedKeysNames;
  }

  public PrimaryKeyRetrievalMode getMode() {
    return mode;
  }

  public String getSequencePreFetchSQL() {
    return sequencePreFetchSQL;
  }

  public String getPrimaryKeyMemberName() {
    return primaryKeyMemberName;
  }

  public String getSequenceInlineSQL() {
    return this.sequenceInlineSQL;
  }

  public String getOutputClause() {
    return outputClause;
  }

  public String[] getGeneratedKeysNames() {
    return generatedKeysNames;
  }

  @Override
  public String toString() {
    return "InsertMechanics [mode=" + mode + ", sequencePreFetchSQL=" + sequencePreFetchSQL + ", primaryKeyMemberName="
        + primaryKeyMemberName + ", sequenceInlineSQL=" + sequenceInlineSQL + ", outputClause=" + outputClause
        + ", generatedKeysNames=" + Arrays.toString(generatedKeysNames) + "]";
  }

}
