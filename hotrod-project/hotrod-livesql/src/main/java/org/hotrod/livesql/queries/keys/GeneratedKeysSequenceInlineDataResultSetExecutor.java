package org.hotrod.livesql.queries.keys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class GeneratedKeysSequenceInlineDataResultSetExecutor<T> extends GeneratedKeysInsertExecutor<T> {

  private static final Logger log = Logger.getLogger(GeneratedKeysSequenceInlineDataResultSetExecutor.class.getName());

  private String sequenceInlineSQL;
  private EntityColumnMetaData keyColumn;
  private String outputClausePrefix;

  public GeneratedKeysSequenceInlineDataResultSetExecutor(KeyReader<T> keyReader, String sequenceInlineSQL,
      EntityColumnMetaData keyColumn, String outputClausePrefix) {
    super(keyReader);
    log.fine("init");
    this.sequenceInlineSQL = sequenceInlineSQL;
    this.keyColumn = keyColumn;
    this.outputClausePrefix = outputClausePrefix;
  }

  @Override
  public InsertSelectRenderingEdits getInsertSelectEdits(List<EntityColumn> columns) throws LiveSQLException {
    boolean included = false;
    for (EntityColumn c : columns) {
      if (c.getName().getName().equals(this.keyColumn.getName().getName())) {
        included = true;
      }
    }
    if (included) {
      throw new LiveSQLException("A LiveSQL INSERT that uses an inline sequence to generate the primary key "
          + "on a table cannot explicitly include this primary key column; "
          + "it's added automatically behind the scenes.");
    }
    return InsertSelectRenderingEdits.of(this.keyColumn, new CharSQLInjection(this.sequenceInlineSQL),
        this.outputClausePrefix);
  }

  @Override
  public T executeOne(LiveSQLPreparedQuery query, Connection conn) throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(query.getSQL())) {
      super.applyParameters(query, ps);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return super.keyReader.read(rs, 1);
        }
        return null;
      }
    }
  }

  @Override
  public InsertResult<T> executeList(LiveSQLPreparedQuery query, Connection conn)
      throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(query.getSQL())) {
      super.applyParameters(query, ps);
      List<T> keys = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          keys.add(super.keyReader.read(rs, 1));
        }
        return new InsertResult<T>(-1, keys);
      }
    }
  }

}
