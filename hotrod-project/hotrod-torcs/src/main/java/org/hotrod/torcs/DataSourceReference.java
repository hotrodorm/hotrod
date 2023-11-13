package org.hotrod.torcs;

import javax.sql.DataSource;

public class DataSourceReference {

  private int id;
  private DataSource dataSource;

  protected DataSourceReference(final int id, final DataSource dataSource) {
    this.id = id;
    this.dataSource = dataSource;
  }

  public int getId() {
    return id;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

}
