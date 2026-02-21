package app;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.queries.InsertResult;
import org.hotrod.livesql.queries.select.tuples.gen.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.BranchDAO;
import app.persistence.dao.BranchDAO.BranchTable;
import app.persistence.dao.CandidateDAO;
import app.persistence.dao.CandidateDAO.CandidateTable;
import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.model.Branch;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private BranchDAO branchDAO;

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private CandidateDAO candidateDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args).close();
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoLiveSQLSelectRows();
      demoLiveSQLSelectTuples();
      demoLiveSQLInsertValues();
      demoLiveSQLInsertSelect();
      demoLiveSQLUpdate();
      demoLiveSQLDelete();
    };
  }

  private void demoLiveSQLSelectRows() {
    EmployeeTable e = this.employeeDAO.newTable();
    List<Row> rows = this.sql.select().from(e).where(e.lastName.lower().like("%smith%").and(e.firstName.like("%e%")))
        .orderBy(e.branchId.desc()).execute();
    for (Row r : rows) {
      System.out.println("1. LiveSQL SELECT ROWS: " + r);
    }
  }

  private void demoLiveSQLSelectTuples() {
    EmployeeTable e = this.employeeDAO.newTable();
    BranchTable b = this.branchDAO.newTable();
    List<Tuple2<Employee, Branch>> tuples = this.sql.select().tuples().from(e).join(b, b.id.eq(e.branchId))
        .where(e.lastName.lower().like("%smith%").and(e.firstName.like("%e%"))).orderBy(e.branchId.desc()).execute();
    for (Tuple2<Employee, Branch> t : tuples) {
      System.out.println("2. LiveSQL SELECT TUPLES:");
      System.out.println("** Employee=" + t.getA());
      System.out.println("** Branch=" + t.getB());
    }
  }

  private void demoLiveSQLInsertValues() {
    EmployeeTable e = this.employeeDAO.newTable();
    int id = this.sql.insert(e).columns(e.firstName, e.lastName, e.branchId)
        .values(sql.val("Bob"), sql.val("Marley"), sql.val(52)).execute();
    System.out.println("3. LiveSQL INSERT-VALUES -- generated id: " + id);
  }

  private void demoLiveSQLInsertSelect() {
    EmployeeTable e = this.employeeDAO.newTable();
    CandidateTable c = this.candidateDAO.newTable();
    InsertResult<Integer> r = this.sql.insert(e).columns(e.firstName, e.lastName, e.branchId)
        .select(sql.select(c.firstName, c.lastName, sql.val(54)).from(c).where(c.accepted.eq(1))).execute();
    System.out.println("4. LiveSQL INSERT-SELECT -- count: " + r.getCount() + " -- generated ids: "
        + r.getKeys().stream().map(k -> "" + k).collect(Collectors.joining(",")));
  }

  private void demoLiveSQLUpdate() {
    EmployeeTable e = this.employeeDAO.newTable();
    Integer[] ids = Arrays.asList(1, 2, 3, 4).toArray(new Integer[0]);
    int count = this.sql.update(e).set(e.branchId, 51)
        .where(e.lastName.lower().like("%smith%").and(e.branchId.in(ids)).and(e.branchId.eq(52))).execute();
    System.out.println("5. LiveSQL UPDATE - count: " + count);
  }

  private void demoLiveSQLDelete() {
    EmployeeTable e = this.employeeDAO.newTable();
    int count = this.sql.delete(e).where(e.branchId.eq(57)).execute();
    System.out.println("6. LiveSQL DELETE - count: " + count);
  }

}
