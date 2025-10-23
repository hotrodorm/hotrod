package app;

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
    SpringApplication.run(App.class, args);
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
    List<Row> rows = this.sql
        .select()
        .from(e)
        .where(e.lastName.lower().like("%smith%").and(e.firstName.like("%e%")))
        .orderBy(e.branchId.desc())
        .execute();
    for (Row r : rows) {
      System.out.println("1. LiveSQL SELECT: " + r);
    }
  }

  private void demoLiveSQLInsert() {
    Employee emp = new Employee();
    emp.setFirstName("Bob");
    emp.setLastName("Marley");
    emp.setBranchId(420);
    Employee inserted = this.employeeDAO.insert(emp);
    System.out.println("2. LiveSQL INSERT: " + inserted);
  }

  private void demoLiveSQLUpdate() {
    EmployeeTable e = this.employeeDAO.newTable();
    Employee values = new Employee();
    values.setBranchId(15);
    int count = this.employeeDAO
        .update(values, e, e.lastName.lower().like("%smith%").and(e.branchId.eq(2)))
        .execute();
    System.out.println("3. LiveSQL UPDATE - rows: " + count);
  }

  private void demoLiveSQLDelete() {
    Employee example = new Employee();
    example.setBranchId(7);
    int count = this.employeeDAO.delete(example);
    System.out.println("4. LiveSQL DELETE - rows: " + count);
  }

}
