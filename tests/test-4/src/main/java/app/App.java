package app;

import java.util.List;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.primitives.EmployeeDAO;
import app.daos.primitives.EmployeeDAO.EmployeeTable;
import app.daos.primitives.StockDAO;
import app.daos.primitives.StockDAO.StockTable;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
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
      System.out.println("[ Starting example ]");

      searching();
      System.out.println("[ Example complete ]");
    };
  }

  private void searching() {

//    // Use CRUD to delete account #123
//
//    AccountTable at = AccountDAO.newTable();
//    AccountVO updateValues = new AccountVO();
//    updateValues.setName("Brand NEW name!");
//    ExecutableQuery q = this.accountDAO.update(updateValues, at.id.eq(123));
//    System.out.println("PREVIEW: " + q.getPreview());
//    q.execute();
//
//    // Use CRUD to search for account #123
//
//    Integer id = 123;
//    AccountVO a = this.accountDAO.select(id);
//    if (a != null) {
//      System.out.println("Account #" + id + " Name: " + a.getName());
//    } else {
//      System.out.println("Account #" + id + " not found.");
//    }

    EmployeeTable e = this.employeeDAO.newTable("e");

    ExecutableSelect<Row> q = this.sql //
        .select(e.star(), sql.val(13).remainder(5).as("rem")) //
        .from(e) //
    ;
    System.out.println("PREVIEW: " + q.getPreview());
    List<Row> rows = q.execute();

    System.out.println("Batches with names that start with 'A':");
    for (Row r : rows) {
      System.out.println(r);
    }

    // Use LiveSQL to search for companies which name start with 'A'

//    BatchTable b = BatchDAO.newTable();
//    EmployeeTable e = EmployeeDAO.newTable();
//
//    ExecutableSelect<Row> q = this.sql.select( //
//        e.star() //
//            .filter(c -> !"BINARY LARGE OBJECT".equals(c.getType()) && !"CHARACTER LARGE OBJECT".equals(c.getType())) //
//            .as(c -> {
//              return Alias.property("emp", c.getProperty());
//            }), //
//        b.star().as(c -> Alias.literal(("bc_" + c.getName()).toLowerCase()))) //
//        .from(e) //
//        .join(b, b.itemName.eq(e.name)) //
//        .where(e.name.like("A%"));
//    System.out.println("PREVIEW: " + q.getPreview());
//    List<Row> rows = q.execute();
//
//    System.out.println("Batches with names that start with 'A':");
//    for (Row r : rows) {
//      System.out.println(r);
//    }

//    CompanyTable c = CompanyDAO.newTable();
//
//    List<Row> rows = this.sql.select().from(c).where(c.name.like("A%")).execute();
//
//    System.out.println("Companies with names that start with 'A':");
//    for (Row r : rows) {
//      System.out.println(r);
//    }

//    // Use LiveSQL to delete rows
//
//    DeleteWherePhase q = this.sql.delete(c).where(c.name.like("%ola"));
//    // System.out.println("Preview: " + q.getPreview());
//    q.execute();
//
//    // Show all companies
//
//    System.out.println("Show all companies:");
//    this.sql.select().from(c).execute().forEach(r -> System.out.println(r));
//
//    // Use LiveSQL to delete rows
//
//    this.sql.delete(c).where(c.name.like("%ola"));
//    System.out.println("Preview: " + q.getPreview());
//    q.execute();
//
//    // Prepend "OK" to all employee names
//    EmployeeTable e = EmployeeDAO.newTable();
//
//    this.sql.update(e).set(e.name, sql.val("OK - ").concat(e.name)).execute();
//    
//    sql.update(e).set(e.name, "a").set(e.name, "b").where(e.name.like("A")).execute();
//
//    // insert VIP employees using VALUES
//
//    EmployeeVipTable ev = EmployeeVipDAO.newTable();
//    this.sql.insert(ev).values(sql.val(7), sql.val("James")).execute();
//
//    this.sql.insert(ev).select(sql.select(sql.val(86), sql.val("Super A")).from(sql.DUAL)).execute();
//
//    // Insert into VIP employees using SELECT
//
//    InsertSelectPhase is = this.sql.insert(ev).select(sql.select(e.id, e.name).from(e).where(e.name.like("%A%")));
//    System.out.println("Preview INSERT: " + is.getPreview());
//    is.execute();
//
//    java.sql.Date.valueOf(LocalDate.of(2015, 01, 01));
//    // Show all VIP employees
//
//    System.out.println("--- Show all VIP employees:");
//    this.sql.select().from(ev).execute().forEach(r -> System.out.println(r));
//
////    // Delete all employees
////
////    this.sql.delete(EmployeeDAO.newTable()).execute();   
////    System.out.println("Show no employees:");
////    this.sql.select().from(EmployeeDAO.newTable()).execute().forEach(r -> System.out.println(r));

  }

  private void searching2() {

    StockTable s = StockDAO.newTable();
    this.sql.insert(s).values(sql.val("FL-5077"), sql.val("TV"), sql.val(20));

  }

}
