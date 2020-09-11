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

import com.myapp1.aopsqlmetrics.SQLMetricsAspect.SQLMetrics;

@Configuration
@ComponentScan
@ImportResource({ "spring-configuration.xml" })

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class App2 {

  @Autowired
  private DataServices2 s2;

  @Autowired
  private SQLMetrics sqlMetrics;

  public static void main(final String[] args) throws SQLException {
    SpringApplication.run(App2.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
    return args -> {

//      Arrays.stream(ctx.getBeanDefinitionNames()).sorted().forEach(n -> System.out.println(" * " + n));

      System.out.println("[ Starting ]");

      this.s2.demoFKs();

      System.out.println("[ Metrics ]");
      System.out.println(this.sqlMetrics.render());

      System.out.println("[ Completed ]");

    };
  }

}
