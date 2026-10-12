package org.hotrod.livesql.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.dynamicsql.DynamicModificationQuery;
import org.hotrod.dynamicsql.DynamicSelectQuery;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.PreparedModificationQuery;
import org.hotrod.dynamicsql.PreparedQuery;
import org.hotrod.dynamicsql.PreparedSelectQuery;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.hotrod.exceptions.PersistenceException;
import org.hotrod.interfaces.OrderBy;
import org.hotrod.utils.SQLUtil;
import org.springframework.jdbc.datasource.DataSourceUtils;

public abstract class DAOExecutor {

  public static <M> M selectOne(DynamicSQL dyn, M filter, DynamicSelectQuery select, RowReader<M> rowReader, Logger log,
      DataSource dataSource) {
    Parameters params = dyn.newParameters();
    params.add("f", filter);
    PreparedSelectQuery<M> preparedQuery = select.prepare(params, rowReader);
    logQuery(preparedQuery, log);
    Connection conn = null;
    try {
      conn = DataSourceUtils.getConnection(dataSource);
      List<M> rows = preparedQuery.execute(conn);
      if (rows.size() == 0)
        return null;
      if (rows.size() == 1)
        return rows.get(0);
      throw new PersistenceException("A single row at most was expected but received " + rows.size() + " rows.");
    } catch (SQLException e) {
      throw new PersistenceException(e);
    } finally {
      if (conn != null) {
        DataSourceUtils.releaseConnection(conn, dataSource);
      }
    }
  }

  public static <M, L> List<M> selectList(DynamicSQL dyn, L example, OrderBy[] orderBies, DynamicSelectQuery select,
      RowReader<M> rowReader, Logger log, DataSource dataSource) {
    Parameters params = dyn.newParameters();
    params.add("e", example);
    String ordering = SQLUtil.render(orderBies);
    params.add("ordering", ordering);
    PreparedSelectQuery<M> preparedQuery = select.prepare(params, rowReader);
    logQuery(preparedQuery, log);
    Connection conn = null;
    try {
      conn = DataSourceUtils.getConnection(dataSource);
      List<M> rows = preparedQuery.execute(conn);
      return rows;
    } catch (SQLException e) {
      throw new PersistenceException(e);
    } finally {
      if (conn != null) {
        DataSourceUtils.releaseConnection(conn, dataSource);
      }
    }
  }

  public static int updateDelete(DynamicModificationQuery update, Parameters params, Logger log, DataSource dataSource) {
    PreparedModificationQuery preparedQuery = update.prepare(params);
    logQuery(preparedQuery, log);
    Connection conn = null;
    try {
      conn = DataSourceUtils.getConnection(dataSource);
      int count = preparedQuery.execute(conn);
      return count;
    } catch (SQLException e) {
      throw new PersistenceException(e);
    } finally {
      if (conn != null) {
        DataSourceUtils.releaseConnection(conn, dataSource);
      }
    }
  }

  public static void logQuery(PreparedQuery preparedQuery, Logger log) {
    if (log.isLoggable(Level.FINER)) {
      log.finest("SQL:\n" + preparedQuery.getPreview(true));
    } else if (log.isLoggable(Level.FINE)) {
      log.fine("SQL:\n" + preparedQuery.getPreview());
    }
  }

}
