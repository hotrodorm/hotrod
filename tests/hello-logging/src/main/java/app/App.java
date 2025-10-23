package app;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.LiveSQLLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.dao.ReportsDAO;
import app.persistence.model.Employee;
import app.persistence.model.FoundEmployee;

@SpringBootApplication
@Configuration
public class App {

  private static final Logger LOG = Logger.getLogger(App.class.getName());

  private static final LiveSQLLogging LIVESQL_LOG = LiveSQLLogging.of(
    () -> LOG.isLoggable(Level.FINE), msg -> LOG.fine(msg),
    () -> LOG.isLoggable(Level.FINER), msg -> LOG.finer(msg)
  );

  @Autowired
  private LiveSQL sql;

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private ReportsDAO reportsDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoCRUD();
      demoNitroSelect();
      demoLiveSQL();
    };
  }

  private void demoCRUD() {
    Employee emp = this.employeeDAO.select(10);
    System.out.println("== Returns ==");
    System.out.println("1. Employee: " + emp);
  }

  private void demoNitroSelect() {
    List<FoundEmployee> fes = this.reportsDAO.findEmployees("t");
    System.out.println("== Returns ==");
    for (FoundEmployee fe : fes) {
      System.out.println("2. Found Employee: " + fe);
    }
  }

  private void demoLiveSQL() {
    EmployeeTable e = this.employeeDAO.newTable();
    List<Row> rows = this.sql.select().from(e).where(e.name.like("%e")).execute(LIVESQL_LOG);
    System.out.println("== Returns ==");
    for (Row r : rows) {
      System.out.println("3. row=" + r);
    }
  }

}
