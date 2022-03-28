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
  private EmployeeDAO employeeDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      runExamples();
      System.out.println("[ Example complete ]");
    };
  }

  private void runExamples() {
    EmployeeVO e = this.employeeDAO.selectByPK(123);
    System.out.println("> Employee Name: " + e.getName());
  }

}
