package org.hotrod.torcs.decorators;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.hotrod.torcs.DataSourceReference;
import org.hotrod.torcs.Torcs;

public class TorcsConnection implements Connection {

  private Connection wrapped;
  private Torcs torcs;
  private DataSourceReference dataSourceReference;

  public TorcsConnection(Connection wrapped, Torcs torcs, DataSourceReference dataSourceReference) {
    if (wrapped == null) {
      throw new RuntimeException("Cannot use a null Connection");
    }
    if (torcs == null) {
      throw new RuntimeException("Cannot use a null Torcs");
    }
    if (dataSourceReference == null) {
      throw new RuntimeException("Cannot use a null DataSourceReference");
    }
    this.wrapped = wrapped;
    this.torcs = torcs;
    this.dataSourceReference = dataSourceReference;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return this.wrapped.isWrapperFor(iface);
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return this.wrapped.unwrap(iface);
  }

  @Override
  public void abort(Executor executor) throws SQLException {
    this.wrapped.abort(executor);
  }

  @Override
  public void clearWarnings() throws SQLException {
    this.wrapped.clearWarnings();
  }

  @Override
  public void close() throws SQLException {
    this.wrapped.close();
  }

  @Override
  public void commit() throws SQLException {
    this.wrapped.commit();
  }

  @Override
  public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
    return this.wrapped.createArrayOf(typeName, elements);
  }

  @Override
  public Blob createBlob() throws SQLException {
    return this.wrapped.createBlob();
  }

  @Override
  public Clob createClob() throws SQLException {
    return this.wrapped.createClob();
  }

  @Override
  public NClob createNClob() throws SQLException {
    return this.wrapped.createNClob();
  }

  @Override
  public SQLXML createSQLXML() throws SQLException {
    return this.wrapped.createSQLXML();
  }

  @Override
  public Statement createStatement() throws SQLException {
    Statement st = this.wrapped.createStatement();
    return new TorcsStatement(st, this, this.torcs, this.dataSourceReference);
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
    Statement st = this.wrapped.createStatement(resultSetType, resultSetConcurrency);
    return new TorcsStatement(st, this, this.torcs, this.dataSourceReference);
  }

  @Override
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
    Statement st = this.wrapped.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    return new TorcsStatement(st, this, this.torcs, this.dataSourceReference);
  }

  @Override
  public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
    return this.wrapped.createStruct(typeName, attributes);
  }

  @Override
  public boolean getAutoCommit() throws SQLException {
    return this.wrapped.getAutoCommit();
  }

  @Override
  public String getCatalog() throws SQLException {
    return this.wrapped.getCatalog();
  }

  @Override
  public Properties getClientInfo() throws SQLException {
    return this.wrapped.getClientInfo();
  }

  @Override
  public String getClientInfo(String name) throws SQLException {
    return this.wrapped.getClientInfo(name);
  }

  @Override
  public int getHoldability() throws SQLException {
    return this.wrapped.getHoldability();
  }

  @Override
  public DatabaseMetaData getMetaData() throws SQLException {
    return this.wrapped.getMetaData();
  }

  @Override
  public int getNetworkTimeout() throws SQLException {
    return this.wrapped.getNetworkTimeout();
  }

  @Override
  public String getSchema() throws SQLException {
    return this.wrapped.getSchema();
  }

  @Override
  public int getTransactionIsolation() throws SQLException {
    return this.wrapped.getTransactionIsolation();
  }

  @Override
  public Map<String, Class<?>> getTypeMap() throws SQLException {
    return this.wrapped.getTypeMap();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return this.wrapped.getWarnings();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return this.wrapped.isClosed();
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    return this.wrapped.isReadOnly();
  }

  @Override
  public boolean isValid(int timeout) throws SQLException {
    return this.wrapped.isValid(timeout);
  }

  @Override
  public String nativeSQL(String sql) throws SQLException {
    return this.wrapped.nativeSQL(sql);
  }

  @Override
  public CallableStatement prepareCall(String sql) throws SQLException {
    CallableStatement cs = this.wrapped.prepareCall(sql);
    return new TorcsCallableStatement(cs, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
    CallableStatement cs = this.wrapped.prepareCall(sql, resultSetType, resultSetConcurrency);
    return new TorcsCallableStatement(cs, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    CallableStatement cs = this.wrapped.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    return new TorcsCallableStatement(cs, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    PreparedStatement ps = this.wrapped.prepareStatement(sql);
    return new TorcsPreparedStatement(ps, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    PreparedStatement ps = this.wrapped.prepareStatement(sql, autoGeneratedKeys);
    return new TorcsPreparedStatement(ps, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    PreparedStatement ps = this.wrapped.prepareStatement(sql, columnIndexes);
    return new TorcsPreparedStatement(ps, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    PreparedStatement ps = this.wrapped.prepareStatement(sql, columnNames);
    return new TorcsPreparedStatement(ps, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
      throws SQLException {
    PreparedStatement ps = this.wrapped.prepareStatement(sql, resultSetType, resultSetConcurrency);
    return new TorcsPreparedStatement(ps, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
      int resultSetHoldability) throws SQLException {
    PreparedStatement ps = this.wrapped.prepareStatement(sql, resultSetType, resultSetConcurrency,
        resultSetHoldability);
    return new TorcsPreparedStatement(ps, this, this.torcs, this.dataSourceReference, sql);
  }

  @Override
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    this.wrapped.releaseSavepoint(savepoint);
  }

  @Override
  public void rollback() throws SQLException {
    this.wrapped.rollback();
  }

  @Override
  public void rollback(Savepoint savepoint) throws SQLException {
    this.wrapped.rollback(savepoint);
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    this.wrapped.setAutoCommit(autoCommit);
  }

  @Override
  public void setCatalog(String catalog) throws SQLException {
    this.wrapped.setCatalog(catalog);
  }

  @Override
  public void setClientInfo(Properties properties) throws SQLClientInfoException {
    this.wrapped.setClientInfo(properties);
  }

  @Override
  public void setClientInfo(String name, String value) throws SQLClientInfoException {
    this.wrapped.setClientInfo(name, value);
  }

  @Override
  public void setHoldability(int holdability) throws SQLException {
    this.wrapped.setHoldability(holdability);
  }

  @Override
  public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    this.wrapped.setNetworkTimeout(executor, milliseconds);
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
    this.wrapped.setReadOnly(readOnly);
  }

  @Override
  public Savepoint setSavepoint() throws SQLException {
    return this.wrapped.setSavepoint();
  }

  @Override
  public Savepoint setSavepoint(String name) throws SQLException {
    return this.wrapped.setSavepoint(name);
  }

  @Override
  public void setSchema(String schema) throws SQLException {
    this.wrapped.setSchema(schema);
  }

  @Override
  public void setTransactionIsolation(int level) throws SQLException {
    this.wrapped.setTransactionIsolation(level);
  }

  @Override
  public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    this.wrapped.setTypeMap(map);
  }

}
