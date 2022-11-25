package com.app;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DataSourcesConfiguration {

  // Database #1 (datasource1, sqlSessionFactory1, and sqlSession1)

  @Bean
  @Primary
  @ConfigurationProperties("datasource1")
  public HikariDataSource dataSource1() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory1(@Qualifier("dataSource1") DataSource dataSource1) throws Exception {

    System.out.println("MySQL: Loading mapper (datasource: "
        + dataSource1.getConnection().getMetaData().getDatabaseProductName() + ")");

    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource1);

    ClassLoader cl = this.getClass().getClassLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] mapperLocations = resolver.getResources("mappers/mysql" + "/**/*.xml");
    factoryBean.setMapperLocations(mapperLocations);

    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession1(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1)
      throws Exception {
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory1);
    return sqlSession;
  }

  // Database #2 (datasource2, sqlSessionFactory2, and sqlSession2)

  @Bean
  @ConfigurationProperties("datasource2")
  public HikariDataSource dataSource2() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSource2") DataSource dataSource2) throws Exception {

    System.out.println(
        "PG: Loading mapper (datasource: " + dataSource2.getConnection().getMetaData().getDatabaseProductName() + ")");

    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource2);

    ClassLoader cl = this.getClass().getClassLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] mapperLocations = resolver.getResources("mappers/postgresql" + "/**/*.xml");
    factoryBean.setMapperLocations(mapperLocations);

    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession2(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory2)
      throws Exception {
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory2);
    return sqlSession;
  }

}