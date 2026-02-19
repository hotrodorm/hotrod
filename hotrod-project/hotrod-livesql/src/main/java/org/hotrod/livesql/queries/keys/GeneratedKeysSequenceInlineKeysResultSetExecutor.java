package org.hotrod.livesql.queries.keys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.character.CharSQLInjection;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public class GeneratedKeysSequenceInlineKeysResultSetExecutor<T> extends GeneratedKeysInsertExecutor<T> {

  private static final Logger log = Logger.getLogger(GeneratedKeysSequenceInlineKeysResultSetExecutor.class.getName());

  private String sequenceInlineSQL;
  private EntityColumn keyColumn;
  private String[] generatedKeysNames;

  public GeneratedKeysSequenceInlineKeysResultSetExecutor(KeyReader<T> keyReader, String sequenceInlineSQL,
      EntityColumn keyColumn, String... generatedKeysNames) {
    super(keyReader);
    log.fine("init");
    this.sequenceInlineSQL = sequenceInlineSQL;
    this.keyColumn = keyColumn;
    this.generatedKeysNames = generatedKeysNames;
  }

  @Override
  public void validateAndPrepareInsertColumns(List<EntityColumn> declaredColumns,
      List<ComparableExpression> declaredValues, List<EntityColumn> preparedColumns,
      List<ComparableExpression> preparedValues) throws LiveSQLException {
    log.info("> validate");
    boolean included = false;
    for (EntityColumn c : declaredColumns) {
      if (c.getCanonicalName().equals(this.keyColumn.getCanonicalName())) {
        included = true;
      }
    }
    log.info("> included=" + included);
    if (included) {
      throw new LiveSQLException("A LiveSQL INSERT that uses an inline sequence to generate the primary key "
          + "on a table cannot explicitly include this primary key column; "
          + "it's added automatically behind the scenes.");
    }

    preparedColumns.add(this.keyColumn);
    preparedColumns.addAll(declaredColumns);

    preparedValues.add(new CharSQLInjection(this.sequenceInlineSQL));
    preparedValues.addAll(declaredValues);
  }

  @Override
  public T executeOne(LiveSQLPreparedQuery query, Connection conn) throws SQLException, DynamicExpressionException {
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
