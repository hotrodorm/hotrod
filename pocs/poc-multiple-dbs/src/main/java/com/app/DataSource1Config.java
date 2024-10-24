package com.app;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialectFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DataSource1Config {

  @Bean
  @Primary
  @ConfigurationProperties("datasource1")
  public HikariDataSource dataSource1() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Value("${datasource1.mappers}")
  private String mappers;

  @Bean
  public SqlSessionFactory sqlSessionFactory1(@Qualifier("dataSource1") DataSource dataSource1) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource1);
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver(this.getClass().getClassLoader())
        .getResources(this.mappers + "/**/*.xml"));
    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession1(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1)
      throws Exception {
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory1);
    return sqlSession;
  }

  @Value("${datasource1.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource1.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource1.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource1.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource1.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQLDialect liveSQLDialect1(@Qualifier("dataSource1") DataSource dataSource1,
      @Qualifier("sqlSession1") SqlSessionTemplate sqlSession1) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource1, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    return liveSQLDialect;
  }

  @Bean
  public MapperFactoryBean<LiveSQLMapper> liveSQLMapper1(
      @Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1) throws Exception {
    MapperFactoryBean<LiveSQLMapper> factoryBean = new MapperFactoryBean<>(LiveSQLMapper.class);
    factoryBean.setSqlSessionFactory(sqlSessionFactory1);
    return factoryBean;
  }

  @Bean
  public LiveSQL liveSQL1(@Qualifier("sqlSession1") SqlSessionTemplate sqlSession1, //
      @Qualifier("liveSQLDialect1") LiveSQLDialect liveSQLDialect1, //
      @Qualifier("liveSQLMapper1") LiveSQLMapper liveSQLMapper1 //
  ) throws Exception {
    LiveSQL ls = new LiveSQL(sqlSession1, liveSQLDialect1, liveSQLMapper1);
    return ls;
  }

}