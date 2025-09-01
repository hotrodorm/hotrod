package app.persistence2;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.LiveSQLDialectFactory;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LayerConfigurationBundleSales {

  @Bean
  public LayerConfiguration layerConfigSales() {
    List<TypeRule> rules = new ArrayList<>();
    return () -> rules;
  }

  @Bean
  @ConfigurationProperties("datasource.sales")
  public DataSourceProperties dataSourcePropertiesSales() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource dataSourceSales(DataSourceProperties dataSourcePropertiesSales) {
    DataSource ds = dataSourcePropertiesSales.initializeDataSourceBuilder().build();
    return ds;
  }

  @Value("${datasource.sales.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource.sales.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource.sales.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource.sales.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource.sales.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQL sales(DataSource dataSourceSales, LayerConfiguration layerConfigSales) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSourceSales, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    LiveSQL ls = new LiveSQL(liveSQLDialect, dataSourceSales, "layerConfigurationSales", layerConfigSales.getTypeRules());
    return ls;
  }

}
