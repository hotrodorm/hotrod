package org.hotrod.torcs;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.torcs.plan.PlanRetrieverFactory;
import org.hotrod.torcs.plan.PlanRetrieverFactory.UnsupportedTorcsDatabaseException;
import org.hotrod.torcs.plan.PlanRetriever;

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

}
