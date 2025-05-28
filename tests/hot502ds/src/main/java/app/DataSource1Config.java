package app;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.LiveSQLDialectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration(proxyBeanMethods = false)
public class DataSource1Config {

  private static final Logger log = Logger.getLogger(DataSource1Config.class.getName());

  @Bean
  @ConfigurationProperties("datasource1")
  public DataSourceProperties dataSource1Properties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource dataSource1(DataSourceProperties dataSource1Properties) {
    DataSource ds = dataSource1Properties.initializeDataSourceBuilder().build();
    log.info("ds1 (" + System.identityHashCode(ds) + "): " + dataSource1Properties.getUrl());
    return ds;
  }

  @Value("${datasource1.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource1.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource1.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource1.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource1.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQLDialect liveSQLDialect1(DataSource dataSource1) throws Exception {
    log.info("lsd1");
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource1, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    log.info("lsd1 - done");
    return liveSQLDialect;
  }

  @Bean
  @Lazy
  public LiveSQL liveSQL1(LiveSQLDialect liveSQLDialect1, DataSource dataSource1) throws Exception {
    log.info("LiveSQL1 -- init -- ds:" + dataSource1);
    LiveSQL ls = new LiveSQL(liveSQLDialect1, dataSource1, "layerConfiguration1");
    log.info("LiveSQL1 -- done");
    return ls;
  }

}