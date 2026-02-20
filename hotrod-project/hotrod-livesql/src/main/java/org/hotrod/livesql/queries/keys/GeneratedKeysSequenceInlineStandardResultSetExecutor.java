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
import org.hotrod.livesql.queries.GeneratedKeysInsertObject.InsertSelectRenderingEdits;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public class GeneratedKeysSequenceInlineStandardResultSetExecutor<T> extends GeneratedKeysInsertExecutor<T> {

  private static final Logger log = Logger.getLogger(GeneratedKeysSequenceInlineKeysResultSetExecutor.class.getName());

  private String sequenceInlineSQL;
  private EntityColumn keyColumn;

  public GeneratedKeysSequenceInlineStandardResultSetExecutor(KeyReader<T> keyReader, String sequenceInlineSQL,
      EntityColumn keyColumn) {
    super(keyReader);
    log.fine("init");
    this.sequenceInlineSQL = sequenceInlineSQL;
    this.keyColumn = keyColumn;
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
    return InsertSelectRenderingEdits.of(this.keyColumn, new CharSQLInjection(this.sequenceInlineSQL), "OUTPUT INSERTED.");
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
  public List<T> executeList(LiveSQLPreparedQuery query, Connection conn)
      throws SQLException, DynamicExpressionException {
    try (PreparedStatement ps = conn.prepareStatement(query.getSQL())) {
      super.applyParameters(query, ps);
      List<T> keys = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          keys.add(super.keyReader.read(rs, 1));
        }
        return keys;
      }
    }
  }

}
