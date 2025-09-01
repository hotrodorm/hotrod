package org.hotrod.livesql.autoconfig;

import java.util.List;

import javax.sql.DataSource;

import org.hotrod.livesql.LShield;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.LiveSQLDialectFactory;
import org.hotrod.livesql.dialects.LiveSQLDialectFactory.LiveSQLDialectException;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiveSQLAutoConfiguration {

  @Bean
  public LiveSQL liveSQL(DataSource dataSource, List<TypeRule> rules) throws LiveSQLDialectException {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource, null, null, null, null, null);
    return LShield.newLiveSQL(liveSQLDialect, dataSource, null, rules);
  }

}
