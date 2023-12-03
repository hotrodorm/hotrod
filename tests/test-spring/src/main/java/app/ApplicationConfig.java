package app;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;

@Configuration
@ComponentScan(basePackages = "app")
public class ApplicationConfig {

  @Bean
  public DataSource dataSource() {
    System.out.println("### DATASOURCE 1");
    DataSource dataSource = null;
    JndiTemplate jndi = new JndiTemplate();
    try {
      dataSource = jndi.lookup("java:comp/env/jdbc/myDS", DataSource.class);
    } catch (NamingException e) {
      e.printStackTrace();
    }
    System.out.println("### DATASOURCE 2 - dataSource=" + dataSource);
    return dataSource;
  }

}