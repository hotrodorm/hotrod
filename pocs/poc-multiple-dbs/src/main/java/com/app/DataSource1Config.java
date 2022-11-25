package com.app;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
public class DataSource1Config {

  @Value("${datasource1.mappers}")
  private String mappers;

  @Bean
  @Primary
  @ConfigurationProperties("datasource1")
  public HikariDataSource dataSource1() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory1(@Qualifier("dataSource1") DataSource dataSource1) throws Exception {

    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource1);

    ClassLoader cl = this.getClass().getClassLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] mapperLocations = resolver.getResources(this.mappers + "/**/*.xml");
    factoryBean.setMapperLocations(mapperLocations);

    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession1(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1)
      throws Exception {
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory1);
    return sqlSession;
  }

}