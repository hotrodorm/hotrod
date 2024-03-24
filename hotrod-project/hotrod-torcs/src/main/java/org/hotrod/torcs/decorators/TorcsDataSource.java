package org.hotrod.torcs.decorators;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.torcs.DataSourceReference;
import org.hotrod.torcs.Torcs;
import org.springframework.beans.factory.annotation.Autowired;

public class TorcsDataSource implements DataSource {

  private static Logger log = Logger.getLogger(TorcsDataSource.class.getName());

  private DataSource wrapped;
  private DataSourceReference dataSourceReference;

  @Autowired
  private Torcs torcs;

  // Constructor

  public TorcsDataSource(DataSource wrapped) {
    if (wrapped == null) {
      throw new RuntimeException("Cannot use a null DataSource");
    }
    this.wrapped = wrapped;
    this.dataSourceReference = DataSourceReference.of(wrapped);
  }

  // DataSource methods

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return this.wrapped.getLogWriter();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return this.wrapped.getLoginTimeout();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return this.wrapped.getParentLogger();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    this.wrapped.setLogWriter(out);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    this.wrapped.setLoginTimeout(seconds);
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
  public Connection getConnection() throws SQLException {
    log.fine("Torcs Connection");
    return new TorcsConnection(this.wrapped.getConnection(), this.torcs, this.dataSourceReference);
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    log.fine("Torcs Connection");
    return new TorcsConnection(this.wrapped.getConnection(username, password), this.torcs, this.dataSourceReference);
  }

}
