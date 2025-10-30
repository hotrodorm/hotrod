package app;

import java.util.List;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.rankings.RankingEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private Torcs torcs;

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      runLightweightSelects1();
      runLightweightSelects2();
      runHeavySelects1();
      runHeavySelects2();
      showRanking();
    };
  }

  private void runLightweightSelects1() {
    int total = 0;
    for (int x = 0; x < 10; x++) {
      Employee emp = this.employeeDAO.select(134081 + x);
      total = total + (emp == null ? 0 : 1);
    }
    System.out.println("Lightweight 1 - rows: " + total);
  }

  private void runLightweightSelects2() {
    int total = 0;
    EmployeeTable e = this.employeeDAO.newTable();
    for (int x = 0; x < 10; x++) {
      List<Employee> rows = this.employeeDAO.select(e, e.firstName.like("%a%").and(e.lastName.length().gt(5)))
          .execute();
      total = total + rows.size();
    }
    System.out.println("Lightweight 1 - rows: " + total);
  }

  private void runHeavySelects1() {
    for (int x = 0; x < 5; x++) {
      // Doing cross join between 6 instances of the same table (~46k rows)
      EmployeeTable e = this.employeeDAO.newTable();
      EmployeeTable f = this.employeeDAO.newTable();
      EmployeeTable g = this.employeeDAO.newTable();
      EmployeeTable h = this.employeeDAO.newTable();
      EmployeeTable i = this.employeeDAO.newTable();
      EmployeeTable j = this.employeeDAO.newTable();
      List<Row> rows = this.sql.select(e.id).from(e).crossJoin(f).crossJoin(g).crossJoin(h).crossJoin(i).crossJoin(j)
          .execute();
      System.out.println("Heavy 1 - rows=" + rows.size());
    }
  }

  private void runHeavySelects2() {
    for (int x = 0; x < 2; x++) {
      // Doing cross join between 8 instances of the same table (~1.6M rows)
      EmployeeTable e = this.employeeDAO.newTable();
      EmployeeTable f = this.employeeDAO.newTable();
      EmployeeTable g = this.employeeDAO.newTable();
      EmployeeTable h = this.employeeDAO.newTable();
      EmployeeTable i = this.employeeDAO.newTable();
      EmployeeTable j = this.employeeDAO.newTable();
      EmployeeTable k = this.employeeDAO.newTable();
      EmployeeTable l = this.employeeDAO.newTable();
      List<Row> rows = this.sql.select(e.firstName).from(e).crossJoin(f).crossJoin(g).crossJoin(h).crossJoin(i)
          .crossJoin(j).crossJoin(k).crossJoin(l).execute();
      System.out.println("Heavy 2 - rows=" + rows.size());
    }
  }

  private void showRanking() {
    System.out.println("Ranking:");
    for (RankingEntry re : this.torcs.getDefaultRanking().getEntries()) {
      System.out.println(re);
    }
  }

}
