package org.hotrod.runtime.livesql.dialects;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DialectConfiguration {

  private static Logger log = Logger.getLogger(DialectConfiguration.class.getName());

  @Value("${livesql.livesqldialectname:#{null}}")
  private String liveSQLDialectName;
  @Value("${livesql.livesqldialectdatabaseName:#{null}}")
  private String liveSQLDialectDatabaseName;
  @Value("${livesql.livesqldialectversionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${livesql.livesqldialectmajorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${livesql.livesqldialectminorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQLDialect liveSQLDialect(DataSource dataSource) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource, this.liveSQLDialectName,
        this.liveSQLDialectDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    log.info("[HotRod auto-config] liveSQLDialect=" + liveSQLDialect);
    return liveSQLDialect;
  }

}