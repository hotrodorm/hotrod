package app;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@MapperScan("org.mybatis.spring.mapper.MapperFactoryBean")
public class Config {

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
    fb.setDataSource(dataSource);
    fb.setTypeHandlersPackage("app");

    fb.setPlugins(new ExamplePlugin());

//    ExampleTypeHandler th = new ExampleTypeHandler ();
//    factoryBean.setTypeHandlers(th);

    SqlSessionFactory s = fb.getObject();
    return s;
  }

}
