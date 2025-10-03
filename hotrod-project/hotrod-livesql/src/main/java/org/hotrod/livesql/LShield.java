package org.hotrod.livesql;

import javax.sql.DataSource;

import org.hotrod.livesql.dialects.LiveSQLDialect;

public class LShield {

  public static LiveSQL newLiveSQL(LiveSQLDialect liveSQLDialect, DataSource dataSource, String qualifier,
      LayerConfiguration layerConfiguration) {
    return new LiveSQL(liveSQLDialect, dataSource, qualifier, layerConfiguration);
  }

  public static DataSource getDataSource(LiveSQL liveSQL) {
    return liveSQL.getDataSource();
  }

  public static LiveSQLDialect getLiveSQLDialect(LiveSQL liveSQL) {
    return liveSQL.getLiveSQLDialect();
  }

}
