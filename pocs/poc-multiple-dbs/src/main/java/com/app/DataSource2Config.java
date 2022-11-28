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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration(proxyBeanMethods = false)
public class DataSource2Config {

  //
  @Bean
  @ConfigurationProperties("datasource2")
  public HikariDataSource dataSource2() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  @Value("${datasource2.mappers}")
  private String mappers;

  @Bean
  public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSource2") DataSource dataSource2) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource2);
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver(this.getClass().getClassLoader())
        .getResources(this.mappers + "/**/*.xml"));
    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession2(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory2)
      throws Exception {
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory2);
    return sqlSession;
  }

  @Value("${datasource2.livesqldialect.name:#{null}}")
  private String liveSQLDialectName;
  @Value("${datasource2.livesqldialect.databaseName:#{null}}")
  private String liveSQLDialectVDatabaseName;
  @Value("${datasource2.livesqldialect.versionString:#{null}}")
  private String liveSQLDialectVersionString;
  @Value("${datasource2.livesqldialect.majorVersion:#{null}}")
  private String liveSQLDialectMajorVersion;
  @Value("${datasource2.livesqldialect.minorVersion:#{null}}")
  private String liveSQLDialectMinorVersion;

  @Bean
  public LiveSQLDialect liveSQLDialect2(@Qualifier("dataSource2") DataSource dataSource2,
      @Qualifier("sqlSession2") SqlSessionTemplate sqlSession2) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource2, this.liveSQLDialectName,
        this.liveSQLDialectVDatabaseName, this.liveSQLDialectVersionString, this.liveSQLDialectMajorVersion,
        this.liveSQLDialectMinorVersion);
    System.out.println(">> liveSQLDialect=" + liveSQLDialect);
    return liveSQLDialect;
  }

  @Bean
  public MapperFactoryBean<LiveSQLMapper> liveSQLMapper2(
      @Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory2) throws Exception {
    MapperFactoryBean<LiveSQLMapper> factoryBean = new MapperFactoryBean<>(LiveSQLMapper.class);
    factoryBean.setSqlSessionFactory(sqlSessionFactory2);
    return factoryBean;
  }

  @Bean
  public LiveSQL liveSQL2(@Qualifier("sqlSession2") SqlSessionTemplate sqlSession2, //
      @Qualifier("liveSQLDialect2") LiveSQLDialect liveSQLDialect2, //
      @Qualifier("liveSQLMapper2") LiveSQLMapper liveSQLMapper2 //
  ) throws Exception {
    LiveSQL ls = new LiveSQL();
    ls.setSqlSession(sqlSession2);
    ls.setSqlDialect(liveSQLDialect2);
    ls.setLiveSQLMapper(liveSQLMapper2);
    System.out.println(">>> sqlSession2=" + sqlSession2);
    System.out.println(">>> liveSQLDialect2=" + liveSQLDialect2);
    System.out.println(">>> liveSQLMapper2=" + liveSQLMapper2);
    return ls;
  }

}