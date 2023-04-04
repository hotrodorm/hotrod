package com.myapp;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.metadata.AllColumns.Alias;
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

import com.myapp.daos.primitives.BranchDAO;
import com.myapp.daos.primitives.BranchDAO.BranchTable;
import com.myapp.daos.primitives.EmployeeDAO;
import com.myapp.daos.primitives.EmployeeDAO.EmployeeTable;
import com.myapp.daos.primitives.QueriesDAO;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private BranchDAO branchDAO;

  @Autowired
  private QueriesDAO queriesDAO;

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

    // Use LiveSQL to search for employees whose name starts with 'A'

    EmployeeTable e = EmployeeDAO.newTable("e");
    BranchTable b = BranchDAO.newTable("b");

    ExecutableSelect<Map<String, Object>> q = this.sql.select( //
        e.star() //
            .filter(c -> !"BINARY LARGE OBJECT".equals(c.getType()) && !"CHARACTER LARGE OBJECT".equals(c.getType())) //
            .as(c -> {
              return Alias.property("emp", c.getProperty());
            }), //
        b.star().as(c -> Alias.literal(("bc_" + c.getName()).toLowerCase()))) //
        .from(e).join(b, b.branchId.eq(e.branchId)).where(e.name.like("A%"));

    System.out.println("q:" + q.getPreview());
    List<Map<String, Object>> rows = q.execute();

    System.out.println("Employees with names that start with 'A':");
    for (Map<String, Object> r : rows) {
      System.out.println(r);
    }

  }

}
