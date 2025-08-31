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
public class DataSource2Config {

  @Bean
  @ConfigurationProperties("datasource2")
  public DataSourceProperties dataSource2Properties() {
    return new DataSourceProperties();
  }

  @Bean("salesd")
  public DataSource dataSource2(DataSourceProperties dataSource2Properties) {
    DataSource ds = dataSource2Properties.initializeDataSourceBuilder().build();
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
  public LiveSQL salesl(DataSource salesd, @Qualifier("sales") LayerConfiguration layerConfiguration) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(salesd, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    LiveSQL ls = new LiveSQL(liveSQLDialect, salesd, "layerConfiguration2", layerConfiguration.getTypeRules());
    return ls;
  }

}
