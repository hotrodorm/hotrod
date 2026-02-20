package org.hotrod.livesql.queries.keys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.queries.GeneratedKeysInsertObject.InsertSelectRenderingEdits;
import org.hotrod.livesql.queries.InsertResult;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

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
//* Preferred Mode for each type
//+ SQL Server only implements IDENTITY ALWAYS; the BY DEFAULT variation is not supported and will crash if a PK value is provided
//^ Sybase ASE: Explicit PK value for an IDENTITY column was not implemented (Use: "SET IDENTITY_INSERT <table> ON" before explicit PK INSERT) 

public abstract class GeneratedKeysInsertExecutor<T> {

  protected KeyReader<T> keyReader;

  protected GeneratedKeysInsertExecutor(KeyReader<T> keyReader) {
    this.keyReader = keyReader;
  }

  public abstract InsertSelectRenderingEdits getInsertSelectEdits(List<EntityColumn> columns) throws LiveSQLException;

  public void applyParameters(LiveSQLPreparedQuery query, PreparedStatement ps) throws SQLException {
    int n = 1;
    for (Object obj : query.getParameters().values()) {
      int i = n++;
      ps.setObject(i, obj);
    }
  }

  public abstract T executeOne(LiveSQLPreparedQuery query, Connection conn) throws SQLException;

  public abstract InsertResult<T> executeList(LiveSQLPreparedQuery query, Connection conn) throws SQLException;

}
