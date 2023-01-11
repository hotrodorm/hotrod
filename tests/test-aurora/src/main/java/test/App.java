package test;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import test.persistence.EmployeeVO;
import test.persistence.primitives.EmployeeDAO;
import test.persistence.primitives.EmployeeDAO.EmployeeTable;

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

    // Use CRUD to search for employee #123

    Integer id = 123;
    EmployeeVO vo = this.employeeDAO.selectByPK(id);
    System.out.println("Employee #" + id + " Name: " + vo.getName());

    // Use LiveSQL to search for employees whose name starts with 'A'

    EmployeeTable e = EmployeeDAO.newTable();

    List<Map<String, Object>> l = this.sql
      .select()
      .from(e)
      .where(e.name.like("A%"))
      .execute();

    System.out.println("Employees with names that start with 'A':");
    for (Map<String, Object> r: l) {
      System.out.println(r);
    }

  }

}