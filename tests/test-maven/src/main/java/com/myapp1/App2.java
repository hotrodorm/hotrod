package com.myapp1;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan
@ImportResource({ "spring-configuration.xml" })

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class App2 {

  @Autowired
  private DataServices2 s2;

  @Autowired
  private ChronoAspect ca;

  public static void main(final String[] args) throws SQLException {
    SpringApplication.run(App2.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
    return args -> {

//      System.out.println("Let's inspect the beans provided by Spring Boot:");
//      String[] beanNames = ctx.getBeanDefinitionNames();
//      Arrays.sort(beanNames);
//      for (String beanName : beanNames) {
//        System.out.println(beanName);
//      }

      System.out.println("[ Starting ]");
//      System.out.println("[ this.ca.getSqlMetrics()=" + this.ca.getSqlMetrics() + " ]");
//      DataServices2 s2 = SpringBeanRetriever.getBean("dataServices2");
      s2.demoFKs();

      System.out.println("[ Metrics ]");
      System.out.println(SQLMetrics.render());

      System.out.println("[ Completed ]");

    };
  }

}
