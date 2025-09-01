package org.hotrod.livesql;

import java.util.List;

import javax.sql.DataSource;

import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.queries.typesolver.TypeRule;

public class LShield {

  public static LiveSQL newLiveSQL(LiveSQLDialect liveSQLDialect, DataSource dataSource, String qualifier,
      List<TypeRule> rules) {
    return new LiveSQL(liveSQLDialect, dataSource, qualifier, rules);
  }

  public static DataSource getDataSource(LiveSQL liveSQL) {
    return liveSQL.getDataSource();
  }

  public static LiveSQLDialect getLiveSQLDialect(LiveSQL liveSQL) {
    return liveSQL.getLiveSQLDialect();
  }

}
