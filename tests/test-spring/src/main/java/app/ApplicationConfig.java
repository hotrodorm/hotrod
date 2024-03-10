package app;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.dialects.LiveSQLDialectFactory;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackageClasses = ApplicationConfig.class)
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@EnableTransactionManagement
public class ApplicationConfig {

  @Bean
  public DataSource dataSource() {
    DataSource dataSource = null;
    JndiTemplate jndi = new JndiTemplate();
    try {
      dataSource = jndi.lookup("java:comp/env/jdbc/myDS", DataSource.class);
    } catch (NamingException e) {
      e.printStackTrace();
    }
    return dataSource;
  }

  @Bean
  public LiveSQLDialect liveSQLDialect(DataSource dataSource) throws Exception {
    LiveSQLDialect liveSQLDialect = LiveSQLDialectFactory.getLiveSQLDialect(dataSource, null, null, null, null, null);
    return liveSQLDialect;
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    return sessionFactory.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession(final SqlSessionFactory sqlSessionFactory) throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

}