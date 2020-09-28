package com.myapp1;

import java.sql.SQLException;
import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class App2 implements
//CommandLineRunner, 
    ApplicationContextAware {

  @Autowired
  private DataServices2 s2;

  @Autowired
  private SQLMetrics sqlMetrics;

  private ApplicationContext appContext;

  private static boolean shutdownRequested;

  @Override
  public void setApplicationContext(final ApplicationContext appContext) throws BeansException {
    this.appContext = appContext;
  }

  public static void main(final String[] args) throws SQLException {
    SpringApplication.run(App2.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {

      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }

    };
  }

//  @Override
//  public void run(final String... args) throws Exception {
//    // printAllBeans();
//
//    Stream.of(args).forEach(a -> System.out.println(" * arg: " + a));
//
//    System.out.println("[ Starting ]");
//
//    this.s2.demoFKs();
//
//    System.out.println("[ Metrics ]");
//    System.out.println(this.sqlMetrics.render());
//
//    // Prepare the shutdown procedure
//
//    shutdownRequested = false;
//
//    Runtime.getRuntime().addShutdownHook(new ShutdownListener());
//
//    while (!shutdownRequested) {
//      Thread.sleep(500);
//    }
//
//    System.out.println("[ Application shutting down ]");
//
//  }

  @SuppressWarnings("unused")
  private void printAllBeans() {
    Arrays.stream(appContext.getBeanDefinitionNames()).sorted().forEach(n -> System.out.println(" * " + n));
  };

  // Shutdown listener

  private static class ShutdownListener extends Thread {

    @Override
    public void run() {
      info("Shutdown requested.");
      shutdownRequested = true;
    }

  }

  private static void info(final String txt) {
    System.out.println("[info]  " + txt);
  }

}
