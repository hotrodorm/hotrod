package app;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.daos.primitives.BranchDAO;
import app.daos.primitives.EmployeeDAO;

public class Beans {

  private static AnnotationConfigApplicationContext context;

  private static Hello hello;
  private static Bye bye;
  private static DataSource dataSource;

  private static LiveSQL liveSQL;
  private static BranchDAO branchDAO;
  private static EmployeeDAO employeeDAO;

  public static void initialize() throws SQLException {
    context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    hello = context.getBean(Hello.class);
    bye = context.getBean(Bye.class);
    dataSource = context.getBean(DataSource.class);

    liveSQL = context.getBean(LiveSQL.class);
    branchDAO = context.getBean(BranchDAO.class);
    employeeDAO = context.getBean(EmployeeDAO.class);
  }

  public static void close() {
    context.close();
  }

  // Getters

  public static Hello getHello() {
    return hello;
  }

  public static Bye getBye() {
    return bye;
  }

  public static DataSource dataSource() {
    return dataSource;
  }

  public static BranchDAO branchDAO() {
    return branchDAO;
  }

  public static EmployeeDAO employeeDAO() {
    return employeeDAO;
  }

  public static LiveSQL liveSQL() {
    return liveSQL;
  }

}
