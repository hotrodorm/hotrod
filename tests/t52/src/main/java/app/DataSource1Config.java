package app;

import javax.sql.DataSource;

import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.LiveSQLDialectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DataSource1Config {

  @Bean
  @ConfigurationProperties("datasource1")
  public DataSourceProperties dataSource1Properties() {
    return new DataSourceProperties();
  }

  @Bean("accountingd")
  public DataSource dataSource1(DataSourceProperties dataSource1Properties) {
    DataSource ds = dataSource1Properties.initializeDataSourceBuilder().build();
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
  public LiveSQL accountingl(DataSource accountingd, @Qualifier("accountingc") LayerConfiguration layerConfiguration)
      throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(accountingd, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    LiveSQL ls = new LiveSQL(liveSQLDialect, accountingd, "layerConfiguration1", layerConfiguration.getTypeRules());
    return ls;
  }

}
