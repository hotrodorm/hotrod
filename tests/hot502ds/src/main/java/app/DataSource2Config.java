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
public class DataSource2Config {

  private static final Logger log = Logger.getLogger(DataSource2Config.class.getName());

  @Bean
  @ConfigurationProperties("datasource2")
  public DataSourceProperties dataSource2Properties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource dataSource2(DataSourceProperties dataSource2Properties) {
    DataSource ds = dataSource2Properties.initializeDataSourceBuilder().build();
    log.info("ds2 (" + System.identityHashCode(ds) + "): " + dataSource2Properties.getUrl());
    return ds;
  }

  @Value("${datasource2.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource2.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource2.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource2.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource2.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQLDialect liveSQLDialect2(DataSource dataSource2) throws Exception {
    log.info("lsd2");
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource2, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    log.info("lsd2 - done");
    return liveSQLDialect;
  }

  @Bean
  @Lazy
  public LiveSQL liveSQL2(LiveSQLDialect liveSQLDialect2, DataSource dataSource2) throws Exception {
    log.info("LiveSQL2 -- init -- ds:" + dataSource2);
    LiveSQL ls = new LiveSQL(liveSQLDialect2, dataSource2, "layerConfiguration2");
    log.info("LiveSQL2 -- done");
    return ls;
  }

}
