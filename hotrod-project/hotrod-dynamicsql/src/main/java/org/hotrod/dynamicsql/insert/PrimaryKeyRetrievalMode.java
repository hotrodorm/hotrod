package org.hotrod.dynamicsql.insert;

public enum PrimaryKeyRetrievalMode {

// Tested Modes for Each Database
//
//                   +--------------------+---------------------------+
//                   |  Using Identities  |      Using Sequences      |
//            +------+--------------------+---------------------------+
// Database   | NR   | IIS  | IIK  | IPOS | SPRE | SIS  | SIK  | SPOS |
// -----------+------+------+------+------+------+------+------+------+
// Oracle     | Yes  |      | Yes* |      | Yes* |      | Yes  |      |
// DB2        | Yes  |      | Yes* |      | Yes  |      | Yes* |      |
// PostgreSQL | Yes  |      | Yes* |      | Yes  |      | Yes* |      |
// SQL Server | Yes  |      | Yes*+|      | Yes  | Yes* | No   |      |
// MySQL      | Yes  |      | Yes* |      | --   | --   | --   | --   |
// MariaDB    | Yes  |      | Yes* |      | --   | --   | --   | --   |
// SybaseASE  | Yes  |      | Yes*^|      | --   | --   | --   | --   |
// H2         | Yes  |      | Yes* |      | Yes  |      | Yes* |      |
// HyperSQL   | Yes  |      | Yes* |      | Yes  |      | Yes* |      |
// Derby      | Yes  |      | Yes* |      | Yes* |      | --   |      |
// -----------+------+------+------+------+------+------+------+------+
//
// * Preferred Mode for each type
// + SQL Server only implements IDENTITY ALWAYS; the BY DEFAULT variation is not supported and will crash if a PK value is provided
// ^ Sybase ASE: Explicit PK value for an IDENTITY column was not implemented (Use: "SET IDENTITY_INSERT <table> ON" before explicit PK INSERT) 

  NO_RETRIEVAL(new PreparedInsertNoRetrievalExecutor(), false), // Implemented

  IDENTITY_INLINE_STANDARD_RESULTSET(null, true), //
  IDENTITY_INLINE_KEYS_RESULTSET(new PreparedInsertIdentityInlineKeyResultSetExecutor(), true), // Implemented
  IDENTITY_POSTFETCH(null, true), //

  SEQUENCE_PREFETCH(new PreparedInsertSequencePreFetchExecutor(), true), // Implemented
  SEQUENCE_INLINE_STANDARD_RESULTSET(new PreparedInsertSequenceInlineStandardResultsetExecutor(), true), // Implemented
  SEQUENCE_INLINE_KEYS_RESULTSET(new PreparedInsertSequenceInlineKeysResultsetExecutor(), true), // Implemented
  SEQUENCE_POSTFETCH(null, true);

  private InsertExecutor insertExecutor;
  private boolean generatesKeys;

  private PrimaryKeyRetrievalMode(InsertExecutor insertExecutor, boolean generatesKeys) {
    this.insertExecutor = insertExecutor;
    this.generatesKeys = generatesKeys;
  }

  public InsertExecutor getInsertExecutor() {
    return this.insertExecutor;
  }

  public final boolean generatesKeys() {
    return generatesKeys;
  }

}
