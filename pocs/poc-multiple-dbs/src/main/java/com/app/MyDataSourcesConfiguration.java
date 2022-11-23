package com.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class MyDataSourcesConfiguration {

//  @Bean
//  @Primary
//  @ConfigurationProperties("datasource1")
//  public DataSourceProperties dataSource1Properties() {
//    return new DataSourceProperties();
//  }
//
//  @Bean
//  @Primary
//  @ConfigurationProperties("datasource1.configuration")
//  public HikariDataSource dataSource1(DataSourceProperties dataSource1Properties) {
//    return dataSource1Properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//  }

//  @Bean
//  @ConfigurationProperties("datasource2")
//  public DataSourceProperties dataSource2Properties() {
//    return new DataSourceProperties();
//  }
//
//  @Bean
//  @ConfigurationProperties("datasource2.configuration")
//  public HikariDataSource dataSource2(DataSourceProperties dataSource2Properties) {
//    System.out.println("dataSource2Properties URL: " + dataSource2Properties.getUrl());
//    return dataSource2Properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//  }

  @Bean
  @Primary
  @ConfigurationProperties("datasource1")
  public HikariDataSource dataSource1() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  @ConfigurationProperties("datasource2")
  public HikariDataSource dataSource2() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

}