package org.hotrod.livesql.queries.keys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.livesql.queries.GeneratedKeysInsertObject.InsertSelectRenderingEdits;
import org.hotrod.livesql.queries.InsertResult;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public class GeneratedKeysIdentityInlineResultSetInsertExecutor<T> extends GeneratedKeysInsertExecutor<T> {

  private static final Logger log = Logger
      .getLogger(GeneratedKeysIdentityInlineResultSetInsertExecutor.class.getName());

  private String[] generatedKeysNames;

  public GeneratedKeysIdentityInlineResultSetInsertExecutor(KeyReader<T> keyReader, String... generatedKeysNames) {
    super(keyReader);
    log.fine("init");
    this.generatedKeysNames = generatedKeysNames;
  }

  @Override
  public InsertSelectRenderingEdits getInsertSelectEdits(List<EntityColumnMetadata> columns) throws LiveSQLException {
    return InsertSelectRenderingEdits.of(null, null, null);
  }

  @Override
  public T executeOne(LiveSQLPreparedQuery query, Connection conn) throws SQLException {
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
  public InsertResult<T> executeList(LiveSQLPreparedQuery query, Connection conn) throws SQLException {
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
