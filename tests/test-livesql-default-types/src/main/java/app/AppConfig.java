package app;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.LiveSQLMapper;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialectFactory;
import org.hotrod.torcs.decorators.TorcsDataSource;
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

@Configuration
public class AppConfig {

  @Bean
  @ConfigurationProperties("spring.datasourceb1")
  @Primary
  // @Qualifier("ds1")
  public DataSource dataSource1() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public TorcsDataSource torcsDataSource1(@Qualifier("dataSource1") DataSource dataSource1) {
    return new TorcsDataSource(dataSource1);
  }

  // Others

  @Value("${datasource1.mappers:mappers}")
  private String mappers;

  @Bean
  public SqlSessionFactory sqlSessionFactory1(TorcsDataSource torcsDataSource1) throws Exception {
    System.out.println("torcsDataSource1:" + System.identityHashCode(torcsDataSource1));
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(torcsDataSource1);
    factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver(this.getClass().getClassLoader())
        .getResources(this.mappers + "/**/*.xml"));
    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession1(@Qualifier("sqlSessionFactory1") SqlSessionFactory sqlSessionFactory1)
      throws Exception {
    SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory1);
    System.out.println("sqlSession:" + System.identityHashCode(sqlSession));
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
  public LiveSQLDialect liveSQLDialect1(TorcsDataSource torcsDataSource1,
      @Qualifier("sqlSession1") SqlSessionTemplate sqlSession1) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(torcsDataSource1, this.liveSQLDialectName,
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
