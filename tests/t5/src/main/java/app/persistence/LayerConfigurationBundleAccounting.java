package app.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.dialects.LiveSQLDialect;
import org.hotrod.livesql.dialects.LiveSQLDialectFactory;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LayerConfigurationBundleAccounting {

  @Bean
  public LayerConfiguration layerConfigAccounting() {
    List<TypeRule> rules = new ArrayList<>();
    rules.add(TypeRule.of("precision < 20", TypeHandler.forClass(Long.class, TypeSource.RUNTIME_LAYER_RULE), 1));
    rules.add(TypeRule.of("scale > 0", TypeHandler.forClass(Short.class, TypeSource.RUNTIME_LAYER_RULE), 2));
    return () -> rules;
  }

  @Bean
  @ConfigurationProperties("datasource.accounting")
  public DataSourceProperties dataSourcePropertiesAccounting() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource dataSourceAccounting(DataSourceProperties dataSourcePropertiesAccounting) {
    DataSource ds = dataSourcePropertiesAccounting.initializeDataSourceBuilder().build();
    return ds;
  }

  @Value("${datasource.accounting.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource.accounting.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource.accounting.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource.accounting.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource.accounting.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQL accounting(DataSource dataSourceAccounting, LayerConfiguration layerConfigAccounting) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSourceAccounting, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    LiveSQL ls = new LiveSQL(liveSQLDialect, dataSourceAccounting, "layerConfigurationAccounting", layerConfigAccounting.getTypeRules());
    return ls;
  }

}
