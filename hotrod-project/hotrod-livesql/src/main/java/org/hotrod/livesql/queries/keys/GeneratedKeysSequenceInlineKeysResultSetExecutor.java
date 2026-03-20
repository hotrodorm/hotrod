package org.hotrod.livesql.queries.keys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.character.CharSQLInjection;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.EntityColumnMetaData;
import org.hotrod.livesql.queries.GeneratedKeysInsertObject.InsertSelectRenderingEdits;
import org.hotrod.livesql.queries.InsertResult;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public class GeneratedKeysSequenceInlineKeysResultSetExecutor<T> extends GeneratedKeysInsertExecutor<T> {

  private static final Logger log = Logger.getLogger(GeneratedKeysSequenceInlineKeysResultSetExecutor.class.getName());

  private String sequenceInlineSQL;
  private EntityColumnMetaData keyColumn;
  private String[] generatedKeysNames;

  public GeneratedKeysSequenceInlineKeysResultSetExecutor(KeyReader<T> keyReader, String sequenceInlineSQL,
      EntityColumnMetaData keyColumn, String... generatedKeysNames) {
    super(keyReader);
    log.fine("init");
    this.sequenceInlineSQL = sequenceInlineSQL;
    this.keyColumn = keyColumn;
    this.generatedKeysNames = generatedKeysNames;
  }

  @Override
  public InsertSelectRenderingEdits getInsertSelectEdits(List<EntityColumn> columns) throws LiveSQLException {
    boolean included = false;
    for (EntityColumn c : columns) {
      if (c.getCanonicalName().equals(this.keyColumn.getCanonicalName())) {
        included = true;
      }
    }
    if (included) {
      throw new LiveSQLException("A LiveSQL INSERT that uses an inline sequence to generate the primary key "
          + "on a table cannot explicitly include this primary key column; "
          + "it's added automatically behind the scenes.");
    }
    return InsertSelectRenderingEdits.of(this.keyColumn, new CharSQLInjection(this.sequenceInlineSQL), null);
  }

  @Override
  public T executeOne(LiveSQLPreparedQuery query, Connection conn) throws SQLException, DynamicExpressionException {
    if (generatedKeysNames == null || generatedKeysNames.length == 0) {
      try (PreparedStatement ps = conn.prepareStatement(query.getSQL(), Statement.RETURN_GENERATED_KEYS)) {
        return executeOne(query, ps, conn);
      }
    } else {
      try (PreparedStatement ps = conn.prepareStatement(query.getSQL(), generatedKeysNames)) {
        return executeOne(query, ps, conn);
      }
    }
  }

  private T executeOne(LiveSQLPreparedQuery query, PreparedStatement ps, Connection conn) throws SQLException {
    super.applyParameters(query, ps);
    ps.executeUpdate();
    try (ResultSet rs = ps.getGeneratedKeys()) {
      if (rs.next()) {
        return super.keyReader.read(rs, 1);
      }
      return null;
    }
  }

  @Override
  public InsertResult<T> executeList(LiveSQLPreparedQuery query, Connection conn)
      throws SQLException, DynamicExpressionException {
    if (generatedKeysNames == null || generatedKeysNames.length == 0) {
      try (PreparedStatement ps = conn.prepareStatement(query.getSQL(), Statement.RETURN_GENERATED_KEYS)) {
        return executeList(query, ps, conn);
      }
    } else {
      try (PreparedStatement ps = conn.prepareStatement(query.getSQL(), generatedKeysNames)) {
        return executeList(query, ps, conn);
      }
    }
  }

  private InsertResult<T> executeList(LiveSQLPreparedQuery query, PreparedStatement ps, Connection conn)
      throws SQLException {
    super.applyParameters(query, ps);
    int count = ps.executeUpdate();
    try (ResultSet rs = ps.getGeneratedKeys()) {
      List<T> keys = new ArrayList<>();
      while (rs.next()) {
        keys.add(super.keyReader.read(rs, 1));
      }
      return new InsertResult<T>(count, keys);
    }
  }

}
