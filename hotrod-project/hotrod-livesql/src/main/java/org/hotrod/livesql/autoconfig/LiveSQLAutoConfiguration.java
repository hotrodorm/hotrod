package org.hotrod.livesql.autoconfig;

import javax.sql.DataSource;

import org.hotrod.livesql.DynamicSQLBean;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.LiveShield;
import org.hotrod.livesql.PersistenceLayerConfigFactory;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.LiveSQLDialectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiveSQLAutoConfiguration {

  @Bean
  @Conditional(OnLiveSQLMissingCondition.class)
  public LiveSQL getLiveSQL() {
    return LiveShield.newLiveSQL();
  }

  @Bean
  @Conditional(OnLiveSQLDialectMissingCondition.class)
  public LiveSQLDialect liveSQLDialect(DataSource dataSource) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource, null, null, null, null, null);
    return liveSQLDialect;
  }

  @Bean
  @Conditional(OnDynamicSQLMissingCondition.class)
  public DynamicSQLBean getDynamicSQL() {
    return new DynamicSQLBean();
  }

  @Bean
  public PersistenceLayerConfigFactory getPersistenceLayerConfigFactory() {
    return new PersistenceLayerConfigFactory();
  }

}
