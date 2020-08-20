package hr3.poc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import hr3.poc.dao.action.NewsImpl;
import hr3.poc.dao.action.primitives.ActionDAO;
import hr3.poc.dao.action.primitives.NewsDAO;

@SpringBootApplication
public class HrAspectPocApplication {
  @Autowired
  ActionDAO adao;
  @Autowired
  NewsDAO ndao;

  public static void main(String[] args) {
    SpringApplication.run(HrAspectPocApplication.class, args);
    // new HrAspectPocApplication().doTheExample();
  }

  private void doTheExample() {
    NewsImpl n = ndao.selectByPK(1L);
    System.out.println(n.getNewsContent());
    // From superclass (load superclass)
    System.out.println(n.getTitle());
    // From superclass (do not load superclass, reuse already-loaded one)
    System.out.println(n.getTitle());

  }

  @Bean
  public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
    return args -> {
      System.out.println("--- Beans provided by Spring Boot ---");
      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(" - " + beanName);
      }
      System.out.println("-------------------------------------");
    };
  }

}
