package org.hotrod.torcs;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hotrod.torcs.plan.PlanRetriever;
import org.hotrod.torcs.plan.PlanRetrieverFactory;
import org.hotrod.torcs.plan.PlanRetrieverFactory.UnsupportedTorcsDatabaseException;

public class DataSourceReference {

  private int id;
  private DataSource dataSource;
  private PlanRetriever planRetriever;

  protected DataSourceReference(final int id, final DataSource dataSource) {
    this.id = id;
    this.dataSource = dataSource;
    this.planRetriever = null;
  }

  public int getId() {
    return id;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public PlanRetriever getPlanRetriever() throws SQLException, UnsupportedTorcsDatabaseException {
    if (this.planRetriever == null) {
      preparePlanRetriever();
    }
    return planRetriever;
  }

  private synchronized void preparePlanRetriever() throws SQLException, UnsupportedTorcsDatabaseException {
    if (this.planRetriever == null) {
      this.planRetriever = PlanRetrieverFactory.getTorcsPlanRetriever(this.dataSource);
    }
  }

  // Instantiation

  private static Map<Integer, DataSourceReference> dataSourcesByHash = new HashMap<>();
  private static int nextReferenceId = 0;

  public boolean multipleDataSources() {
    return this.dataSourcesByHash.size() > 1;
  }

  public static DataSourceReference of(final DataSource dataSource) {
    DataSourceReference r = dataSourcesByHash.get(System.identityHashCode(dataSource));
    if (r == null) {
      r = addDataSourceReference(dataSource);
    }
    return r;
  }

  private static synchronized DataSourceReference addDataSourceReference(final DataSource dataSource) {
    DataSourceReference r = new DataSourceReference(nextReferenceId++, dataSource);
    dataSourcesByHash.put(System.identityHashCode(dataSource), r);
    return r;
  }

}
