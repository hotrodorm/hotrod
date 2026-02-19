package org.hotrod.livesql.queries.keys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.numeric.NumericConstant;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;

public class GeneratedKeysSequencePreFetchInsertExecutor<T> extends GeneratedKeysInsertExecutor<T> {

  private static final Logger log = Logger.getLogger(GeneratedKeysSequencePreFetchInsertExecutor.class.getName());

  private String sequencePreFetchSQL;
  private EntityColumn keyColumn;

  public GeneratedKeysSequencePreFetchInsertExecutor(KeyReader<T> keyReader, String sequencePreFetchSQL,
      EntityColumn keyColumn) {
    super(keyReader);
    log.fine("init");
    this.sequencePreFetchSQL = sequencePreFetchSQL;
    this.keyColumn = keyColumn;
  }

  @Override
  public void validateAndPrepareInsertColumns(List<EntityColumn> declaredColumns,
      List<ComparableExpression> declaredValues, List<EntityColumn> preparedColumns,
      List<ComparableExpression> preparedValues) throws LiveSQLException {
    boolean included = false;
    for (EntityColumn c : declaredColumns) {
      if (c.getCanonicalName().equals(this.keyColumn.getCanonicalName())) {
        included = true;
      }
    }
    if (included) {
      throw new LiveSQLException("A LiveSQL INSERT that uses a sequence to generate the primary key "
          + "on a table cannot explicitly include this primary key column; "
          + "it's added automatically behind the scenes.");
    }

    preparedColumns.add(this.keyColumn);
    preparedColumns.addAll(declaredColumns);

    preparedValues.add(new NumericConstant(null));
    preparedValues.addAll(declaredValues);

  }

  @Override
  public T executeOne(LiveSQLPreparedQuery query, Connection conn) throws SQLException, DynamicExpressionException {

    // 1. Prefetch the sequence value

    T seq = null;
    try (PreparedStatement ps = conn.prepareStatement(this.sequencePreFetchSQL)) {
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          seq = super.keyReader.read(rs, 1);
        }
      }
    }
    if (seq == null) {
      throw new SQLException("Could not retrieve sequence for INSERT using: " + sequencePreFetchSQL);
    }

    // 2. Apply it to the parameter list

    String sequencePlaceholderName = query.getParameters().keySet().iterator().next();
    log.info("sequencePlaceholderName=" + sequencePlaceholderName + " -- seq=" + seq);
    query.getParameters().put(sequencePlaceholderName, seq);

    // 3. Run the INSERT query

    try (PreparedStatement ps = conn.prepareStatement(query.getSQL())) {
      super.applyParameters(query, ps);
      ps.executeUpdate();
      return seq;
    }

  }

}
