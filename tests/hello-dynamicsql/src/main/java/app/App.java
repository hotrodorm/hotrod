package app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.EmployeeDAO;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private EmployeeDAO testDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoNitro1();
    };
  }

  private void demoNitro1() {
    List<Integer> codes = Arrays.asList(123, 456, 789);
    List<Employee> rows = this.testDAO.search1(null);
    for (Employee e : rows) {
      System.out.println("* v=" + e.getFirstName());
    }
  }

}
