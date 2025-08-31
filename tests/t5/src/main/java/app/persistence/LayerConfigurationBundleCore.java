package app.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

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
public class LayerConfigurationBundleCore {

  @Bean
  @ConfigurationProperties("datasource.core")
  public DataSourceProperties dataSourcePropertiesCore() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource dataSourceCore(DataSourceProperties dataSourcePropertiesCore) {
    DataSource ds = dataSourcePropertiesCore.initializeDataSourceBuilder().build();
    return ds;
  }

  @Value("${datasource.core.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource.core.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource.core.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource.core.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource.core.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQL core(DataSource dataSourceCore) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSourceCore, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    
    // Runtime rules

    List<TypeRule> rules = new ArrayList<>();
    rules.add(TypeRule.of("precision < 20", TypeHandler.forClass(Long.class, TypeSource.RUNTIME_LAYER_RULE), 1));
    rules.add(TypeRule.of("scale > 0", TypeHandler.forClass(Short.class, TypeSource.RUNTIME_LAYER_RULE), 2));

    // LiveSQL bean
    
    LiveSQL ls = new LiveSQL(liveSQLDialect, dataSourceCore, "layerConfigurationCore", rules);
    return ls;
  }

}
