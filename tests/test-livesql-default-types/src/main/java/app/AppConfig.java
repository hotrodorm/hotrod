package app;

import javax.sql.DataSource;

import org.hotrod.torcs.decorators.TorcsDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Value("${my.datasource.driver-class-name:#{null}}")
  private String jdbcDriverClassName;

  @Value("${my.datasource.url:#{null}}")
  private String jdbcURL;

  @Value("${my.datasource.username:#{null}}")
  private String jdbcUsername;

  @Value("${my.datasource.password:#{null}}")
  private String jdbcPassword;

  @Bean
  public DataSource dataSource() {
    DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName(this.jdbcDriverClassName);
    dataSourceBuilder.url(this.jdbcURL);
    dataSourceBuilder.username(this.jdbcUsername);
    dataSourceBuilder.password(this.jdbcPassword);
    DataSource originalDataSource = dataSourceBuilder.build();
    return new TorcsDataSource(originalDataSource);
  }

}
