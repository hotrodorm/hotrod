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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DataSource2Config {

  @Value("${datasource2.mappers}")
  private String mappers;

  @Bean
  @ConfigurationProperties("datasource2")
  public HikariDataSource dataSource2() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSource2") DataSource dataSource2) throws Exception {

    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource2);

    ClassLoader cl = this.getClass().getClassLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    Resource[] mapperLocations = resolver.getResources(this.mappers + "/**/*.xml");
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