package org.hotrod.dynamic.insert;

public enum PrimaryKeyRetrievalMode {

  NO_RETRIEVAL(new PreparedInsertNoRetrievalQuery()), // DONE
  SEQUENCE_PREFETCH(new PreparedInsertSequencePreFetchQuery()), // DONE
  SEQUENCE_INLINE_STANDARD_RESULTSET(new PreparedInsertNoRetrievalQuery()), //
  SEQUENCE_INLINE_KEYS_RESULTSET(new PreparedInsertNoRetrievalQuery()), //
  SEQUENCE_POSTFETCH(new PreparedInsertNoRetrievalQuery()), //
  IDENTITY_INLINE_STANDARD_RESULTSET(new PreparedInsertNoRetrievalQuery()), //
  IDENTITY_INLINE_KEYS_RESULTSET(new PreparedInsertIdentityInlineKeyResultSetQuery()), // DONE
  IDENTITY_POSTFETCH(new PreparedInsertNoRetrievalQuery());

  private InsertExecutor insertExecutor;

  private PrimaryKeyRetrievalMode(InsertExecutor insertExecutor) {
    this.insertExecutor = insertExecutor;
  }

  public InsertExecutor getInsertExecutor() {
    return this.insertExecutor;
  }

}
