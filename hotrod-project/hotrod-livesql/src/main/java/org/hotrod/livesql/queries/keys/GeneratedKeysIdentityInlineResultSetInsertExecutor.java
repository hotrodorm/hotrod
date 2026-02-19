package org.hotrod.livesql.queries.keys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public class GeneratedKeysIdentityInlineResultSetInsertExecutor<T> extends GeneratedKeysInsertExecutor<T> {

  private String[] generatedKeysNames;

  public GeneratedKeysIdentityInlineResultSetInsertExecutor(KeyReader<T> keyReader, String... generatedKeysNames) {
    super(keyReader);
    this.generatedKeysNames = generatedKeysNames;
  }

  @Override
  public void validateAndPrepareInsertColumns(List<EntityColumn> declaredColumns,
      List<ComparableExpression> declaredValues, List<EntityColumn> preparedColumns,
      List<ComparableExpression> preparedValues) throws LiveSQLException {
    // Nothing to do
  }

  @Override
  public T executeOne(LiveSQLPreparedQuery query, Connection conn) throws SQLException {
    if (generatedKeysNames == null || generatedKeysNames.length == 0) {
      try (PreparedStatement ps = conn.prepareStatement(query.getSQL(), Statement.RETURN_GENERATED_KEYS)) {
        return execute(query, ps, conn);
      }
    } else {
      try (PreparedStatement ps = conn.prepareStatement(query.getSQL(), generatedKeysNames)) {
        return execute(query, ps, conn);
      }
    }
  }

  private T execute(LiveSQLPreparedQuery query, PreparedStatement ps, Connection conn) throws SQLException {
    super.applyParameters(query, ps);
    ps.executeUpdate();
    try (ResultSet rs = ps.getGeneratedKeys()) {
      if (rs.next()) {
        return super.keyReader.read(rs, 1);
      }
      return null;
    }
  }

}
