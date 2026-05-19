package app;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.AppConfiguration.Plan;
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
//      search1();
//      search2();
//      search3();
//      search4();
//      search5();
//      search6();
//      search7();
      update1();
    };
  }

  private void search1() {
    Search1Filter filter = new Search1Filter();
    filter.setFilterActive(true);
    filter.setLastName("Smith");
    List<Employee> rows = this.testDAO.search1(filter);
    for (Employee e : rows) {
      System.out.println("* v=" + e.getFirstName());
    }
  }

  private void search2() {
    List<Employee> rows = this.testDAO.search2(2);
    for (Employee e : rows) {
      System.out.println("* v=" + e.getFirstName());
    }
  }

//  List<Integer> ids = Arrays.asList(123, 456, 789);

  private void search3() {
    Integer[] ids = { 123, 456, 789 };
    List<Employee> rows = this.testDAO.search3(ids);
    for (Employee e : rows) {
      System.out.println("* v=" + e.getFirstName());
    }
  }

  private void search4() {
    Map<Integer, String[]> filter = new HashMap<>();
    filter.put(103, new String[] { "abc", "def", "ghi" });
    filter.put(122, new String[] { "jkl" });
    List<Employee> rows = this.testDAO.search4(filter);
    for (Employee e : rows) {
      System.out.println("* v=" + e.getFirstName());
    }
  }

  private void search5() {
    String partialName = "mi";

    AppConfiguration config = new AppConfiguration();
    Map<String, Plan> plans = new HashMap<>();
    plans.put("basicPlan", Plan.of(null, false, "Alchemist", true));
    config.setPlans(plans);

    List<Employee> rows = this.testDAO.search5(partialName, config);
    for (Employee e : rows) {
      System.out.println("* v=" + e.getFirstName());
    }
  }

  private void search6() {
    List<Employee> rows = this.testDAO.search6(true, false, true);
    for (Employee e : rows) {
      System.out.println("* v=" + e.getFirstName());
    }
  }

  private void search7() {
  List<Employee> rows = this.testDAO.search7("Peter", null, LocalDate.of(2025, 6, 12));
    for (Employee e : rows) {
      System.out.println("* v=" + e.getFirstName());
    }
  }

  private void update1() {
    Integer[] ids = { 123, 456, 789 };
    String newStatus = "ACT";
    LocalDate hiredOn = LocalDate.of(2026, 9, 13);
    Integer cityId = 56;
    int count = this.testDAO.update1(newStatus, hiredOn, cityId);
    System.out.println("* count=" + count);
  }

}
