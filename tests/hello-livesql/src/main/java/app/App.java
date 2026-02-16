package app;

import java.util.Arrays;
import java.util.List;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
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
  private EmployeeDAO employeeDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoLiveSQLSelect();
      demoLiveSQLInsert();
      demoLiveSQLUpdate();
      demoLiveSQLDelete();
    };
  }

  private void demoLiveSQLSelect() {
    EmployeeTable e = this.employeeDAO.newTable();
    List<Row> rows = this.sql.select().from(e).where(e.lastName.lower().like("%smith%").and(e.firstName.like("%e%")))
        .orderBy(e.branchId.desc()).execute();
    for (Row r : rows) {
      System.out.println("1. LiveSQL SELECT: " + r);
    }
  }

  private void demoLiveSQLInsert() {
    EmployeeTable e = this.employeeDAO.newTable();
    int count = this.sql.insert(e).columns(e.firstName, e.lastName, e.branchId)
        .values(sql.val("Bob"), sql.val("Marley"), sql.val(420)).execute();
    System.out.println("2. LiveSQL INSERT count: " + count);
  }

  private void demoLiveSQLUpdate() {
    EmployeeTable e = this.employeeDAO.newTable();
    Integer[] ids = Arrays.asList(1, 2, 3, 4).toArray(new Integer[0]);
    int count = this.sql.update(e).set(e.branchId, 15)
        .where(e.lastName.lower().like("%smith%").and(e.branchId.in(ids)).and(e.branchId.eq(2))).execute();
    System.out.println("3. LiveSQL UPDATE - count: " + count);
  }

  private void demoLiveSQLDelete() {
    EmployeeTable e = this.employeeDAO.newTable();
    int count = this.sql.delete(e).where(e.branchId.eq(7)).execute();
    System.out.println("4. LiveSQL DELETE - count: " + count);
  }

}
