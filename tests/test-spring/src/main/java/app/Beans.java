package app;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Beans {

  private static AnnotationConfigApplicationContext context;

  private static Hello hello;
  private static Bye bye;
  private static DataSource dataSource;

  public static void initialize() throws SQLException {
    context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    hello = context.getBean(Hello.class);
    bye = context.getBean(Bye.class);
    dataSource = context.getBean(DataSource.class);
  }

  public static void close() {
    context.close();
  }

  public static Hello getHello() {
    return hello;
  }

  public static Bye getBye() {
    return bye;
  }

  public static DataSource dataSource() {
    return dataSource;
  }

}
