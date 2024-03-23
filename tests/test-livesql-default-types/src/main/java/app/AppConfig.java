package app;

import javax.sql.DataSource;

import org.hotrod.torcs.decorators.TorcsDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  @ConfigurationProperties("spring.datasource")
  public DataSourceProperties dataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource dataSource(DataSourceProperties properties) {
    return new TorcsDataSource(properties.initializeDataSourceBuilder().build());
  }

}
