package app;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.daos.primitives.BranchDAO;
import app.daos.primitives.EmployeeDAO;
import app.logic.BusinessLogic;

public class Beans {

  private static AnnotationConfigApplicationContext context;

  private static LiveSQL liveSQL;
  private static BranchDAO branchDAO;
  private static EmployeeDAO employeeDAO;

  private static BusinessLogic logic;

  public static void initialize() {
    context = new AnnotationConfigApplicationContext(ApplicationConfig.class, LiveSQL.class);

    liveSQL = context.getBean(LiveSQL.class);
    branchDAO = context.getBean(BranchDAO.class);
    employeeDAO = context.getBean(EmployeeDAO.class);

    logic = context.getBean(BusinessLogic.class);
  }

  public static void close() {
    context.close();
  }

  // Getters

  public static BranchDAO branchDAO() {
    return branchDAO;
  }

  public static EmployeeDAO employeeDAO() {
    return employeeDAO;
  }

  public static LiveSQL liveSQL() {
    return liveSQL;
  }

  public static BusinessLogic logic() {
    return logic;
  }

}
