package ${package};

import java.sql.SQLException;
import java.util.List;

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
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.myapp.persistence.primitives.EmployeeDAO;
import com.myapp.persistence.EmployeeVO;

@Configuration
@ComponentScan(basePackageClasses = App.class)
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class App {
  
  @Autowired
  private DatabaseVerifier databaseVerifier;

  @Autowired
  private EmployeeDAO employeeDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      
      // To enable the database verifier, first 1) take a database snapshot, and 2) uncomment the line below
      // this.databaseVerifier.verify();
      
      System.out.println("[ Starting example ]");
      runExamples();
      System.out.println("[ Example complete ]");
    };
  }

  private void runExamples() {
    Integer id = 123;
    EmployeeVO e = this.employeeDAO.selectByPK(id);
    System.out.println("> Employee #" + id + " Name: " + e.getName());
  }

}
