package com.app;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DialectConfig {

  @Bean
  public LiveSQLDialect liveSQLDialect() throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(null, "MYSQL", "MySQL Database", "8.0", "8",
        "0");
    System.out.println(">> liveSQLDialect=" + liveSQLDialect);
    return liveSQLDialect;
  }

}