package app;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
      example1();
      example2();
      example3();
      example4();
      example5();
      example6();
      example7();
      example8();
    };
  }

  private void example1() {
    System.out.println("Example 1 - Using <if>: Assembling a dynamic search predicate");
    Search1Filter filter = new Search1Filter();
    filter.setFilterActive(true);
    filter.setLastName("Smith");
    List<Employee> rows = this.testDAO.search1(filter);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example2() {
    System.out.println("Example 2 - Using <choose>: Select different ordering at runtime");
    List<Employee> rows = this.testDAO.search2(2);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example3() {
    System.out.println("Example 3 - Simple <foreach>: Iterate over an array");
    Integer[] ids = { 170, 223, 640 };
    List<Employee> rows = this.testDAO.search3(ids);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example4() {
    System.out.println("Example 4 - Advanced <foreach>: Assemble complex predicates by nesting <foreach>");
    Map<Integer, String[]> filter = new HashMap<>();
    filter.put(14, new String[] { "Nurse", "Medic", "Engineer" });
    filter.put(27, new String[] { "Virtual Assistant" });
    List<Employee> rows = this.testDAO.search4(filter);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example5() {
    System.out.println("Example 5 - Using <bind>: Using variables to simplify expressions and property navigation");

    String partialName = "mi";

    AppConfiguration config = new AppConfiguration();
    Map<String, Plan> plans = new HashMap<>();
    plans.put("basicPlan", Plan.of(null, "Medic", true, true));
    config.setPlans(plans);

    List<Employee> rows = this.testDAO.search5(partialName, config);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example6() {
    System.out.println("Example 6 - Using <trim>: Assemble dynamic lists of segments using separators");
    List<Employee> rows = this.testDAO.search6(true, false, true);
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example7() {
    System.out.println("Example 7 - Using <where>: Assemble a dynamic WHERE predicate");
    List<Employee> rows = this.testDAO.search7("Peter", null, LocalDate.of(2025, 6, 12));
    System.out.println("* Found " + rows.size() + " employees: "
        + rows.stream().map(r -> "id=" + r.getId()).collect(Collectors.joining(", ")));
  }

  private void example8() {
    System.out.println("Example 8 - Using <set>: Assemble a dynamic SET clause in an UPDATE query");
    String newStatus = "A";
    Integer cityId = 27;
    LocalDate hiredOn = LocalDate.of(2026, 9, 13);
    int count = this.testDAO.update1(newStatus, hiredOn, cityId);
    System.out.println("* Update count=" + count);
  }

}
