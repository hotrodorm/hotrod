package app;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.BranchDAO;
import app.persistence.dao.BranchDAO.BranchTable;
import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private BranchDAO branchDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      demoCRUD();
      demoLiveSQL();
      System.out.println("[ Example complete ]");
    };
  }

  private void demoCRUD() throws DynamicExpressionException, SQLException {
    Employee emp = this.employeeDAO.select(134081);
    System.out.println("Employee #123081's name: " + emp.getFirstName());
  }

  private void demoLiveSQL() {

    System.out.println("Employees with last names that include smith from branches of type 2, 6, or 7:");

    EmployeeTable e = this.employeeDAO.newTable("e");
    BranchTable b = this.branchDAO.newTable("b");

    List<Row> rows = this.sql
      .select(e.star(), b.name.as("branchName"))
      .from(e)
      .join(b, b.id.eq(e.branchId))
      .where(e.lastName.lower().like("%smith%").and(b.type.in(2, 6, 7)))
      .orderBy(b.name, e.lastName.desc())
      .execute();

    for (Row r : rows) {
      System.out.println(r);
    }

  }

}
