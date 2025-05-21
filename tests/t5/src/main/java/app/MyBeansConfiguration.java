package app;

import javax.sql.DataSource;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialectFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import com.zaxxer.hikari.HikariDataSource;

//@Configuration
public class MyBeansConfiguration {

////  @Bean
////  @ConfigurationProperties
////  public HikariDataSource dataSource() {
////    return DataSourceBuilder.create().type(HikariDataSource.class).build();
////  }
//
//  @Bean
//  @ConfigurationProperties("app.datasource")
//  public HikariDataSource dataSource() {
//    return DataSourceBuilder.create().type(HikariDataSource.class).build();
//  }
//
//////  @Bean
//////  @ConfigurationProperties("app.datasource")
////  public DataSource dataSource() {
////    System.out.println("###### DS 1");
//////    return DataSourceBuilder.create().build();
////    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
////    dataSourceBuilder.driverClassName("org.h2.Driver");
////    dataSourceBuilder.url("jdbc:h2:mem:EXAMPLEDB;INIT=runscript from 'src/main/database/h2/1.0.0/build.sql';DB_CLOSE_DELAY=-1");
////    dataSourceBuilder.username("sa");
////    dataSourceBuilder.password("");
////    return dataSourceBuilder.build();
////  }
//
////  @Bean
////  public DataSource getDataSource() {
////    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
////    return dataSourceBuilder.build();
////  }
//
//  @Bean
//  public LiveSQLDialect liveSQLDialect(DataSource dataSource) throws Exception {
//    System.out.println("dataSource=" + dataSource);
//    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource, null, null, null, null, null);
//    return liveSQLDialect;
//  }
//
//  @Bean
//  public LiveSQL liveSQL2(LiveSQLDialect liveSQLDialect, DataSource dataSource) throws Exception {
//    LiveSQL ls = new LiveSQL(liveSQLDialect, dataSource);
//    return ls;
//  }

}
