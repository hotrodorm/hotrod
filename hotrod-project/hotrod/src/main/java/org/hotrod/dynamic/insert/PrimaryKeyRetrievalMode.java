package org.hotrod.dynamic.insert;

public enum PrimaryKeyRetrievalMode {

// Tested Modes for Each Database
//
//                   +--------------------+---------------------------+
//                   |  Using Identities  |      Using Sequences      |
//            +------+--------------------+---------------------------+
// Database   | NR   | IIS  | IIK  | IPOS | SPRE | SIS  | SIK  | SPOS |
// -----------+------+------+------+------+------+------+------+------+
// Oracle     | Yes  |      | Yes* |      | Yes  |      | Yes* |      |
// DB2        | Yes  |      | Yes* |      | Yes  |      | Yes* |      |
// PostgreSQL | Yes  |      | Yes* |      |      |      | Yes* |      |
// SQL Server | Yes  |      |      |      |      |      |      |      |
// MySQL      | Yes  |      |      |      |      |      |      |      |
// MariaDB    | Yes  |      |      |      |      |      |      |      |
// SybaseASE  | Yes  |      |      |      |      |      |      |      |
// H2         | Yes  |      | Yes* |      | Yes  |      | Yes* |      |
// HyperSQL   | Yes  |      |      |      |      |      |      |      |
// Derby      | Yes  |      |      |      |      |      |      |      |
// -----------+------+------+------+------+------+------+------+------+
//
// * Preferred Mode for each type

  NO_RETRIEVAL(new PreparedInsertNoRetrievalQuery()), // DONE

  IDENTITY_INLINE_STANDARD_RESULTSET(null), //
  IDENTITY_INLINE_KEYS_RESULTSET(new PreparedInsertIdentityInlineKeyResultSetQuery()), // DONE
  IDENTITY_POSTFETCH(null), //

  SEQUENCE_PREFETCH(new PreparedInsertSequencePreFetchQuery()), // DONE
  SEQUENCE_INLINE_STANDARD_RESULTSET(null), //
  SEQUENCE_INLINE_KEYS_RESULTSET(new PreparedInsertSequenceInlineKeysResultsetQuery()), // DONE
  SEQUENCE_POSTFETCH(null);

  private InsertExecutor insertExecutor;

  private PrimaryKeyRetrievalMode(InsertExecutor insertExecutor) {
    this.insertExecutor = insertExecutor;
  }

  public InsertExecutor getInsertExecutor() {
    return this.insertExecutor;
  }

}
