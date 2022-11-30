package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DialectConfiguration {

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
  public LiveSQLDialect liveSQLDialect() throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(null, this.liveSQLDialectName,
        this.liveSQLDialectDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    System.out.println("[auto-config] liveSQLDialect=" + liveSQLDialect);
    return liveSQLDialect;
  }

}