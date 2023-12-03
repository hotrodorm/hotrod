package app;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//Minimal:
//spring-core 
//spring-beans

//Then:
//spring-context
//spring-context-support
//spring-expression 

@Configuration
@Component
public class App {

  public static void main(String[] args) throws SQLException {
    new App().basicComponents();
  }

  private void basicComponents() throws SQLException {
    try (
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);) {
      Hello h = context.getBean("hello", Hello.class);
      h.sayHello();
      Bye b = context.getBean("bye", Bye.class);
      b.sayBye();

      ResultSet rs = h.getDataSource().getConnection().createStatement().executeQuery("select count(*) from branch");
      while (rs.next()) {
        System.out.println("rs=" + rs.getString(1));
      }
    }
  }

}
