package app;

import java.util.List;

import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.EmployeeDAO;
import app.persistence.dao.EmployeeDAO.EmployeeOrderBy;
import app.persistence.dao.EmployeeDAO.EmployeeTable;
import app.persistence.model.Employee;

@SpringBootApplication
@Configuration
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

      selectByPrimaryKey();
      selectByExample();
      selectByCriteria();

      insert();
      insertByExample();

      updateByPrimayKey();
      updateByExample();
      updateByCriteria();

      deleteByPrimaryKey();
      deleteByExample();
      deleteByCriteria();

    };
  }

  private void selectByPrimaryKey() {
    Employee emp = this.employeeDAO.select(123);
  }

  private void selectByExample() {
    Employee example = new Employee();
    example.setBranchId(2);
    // Selecting by example without ordering
    List<Employee> employees = this.employeeDAO.select(example);
    // You can add simple sorting criteria
    List<Employee> employees2 = this.employeeDAO.select(example, EmployeeOrderBy.LAST_NAME$DESC,
        EmployeeOrderBy.FIRST_NAME);
  }

  private void selectByCriteria() {
    EmployeeTable e = this.employeeDAO.newTable();
    // Using a complex search criteria
    List<Employee> employees = this.employeeDAO.select(e, e.salary.between(70, 90).and(e.lastName.like("%e%")))
        .execute();
    // Adding ORDER BY, OFFSET, LIMIT, FOR UPDATE, SKIP LOCKED
    List<Employee> employees2 = this.employeeDAO.select(e, e.salary.between(70, 90).and(e.lastName.like("%e%"))) //
        .orderBy(sql.caseWhen(e.branchId.between(10, 19), 0).elseValue(1).end(), e.salary.desc()) //
        .offset(2) //
        .limit(1) //
        .forUpdate() //
        .skipLocked() //
        .execute();
  }

  private void insert() {
    Employee emp = new Employee();
    // Auto-generated ID, blank salary, blank branch_id
    emp.setFirstName("Catherine");
    emp.setLastName("De Bourgh");
    Employee inserted = this.employeeDAO.insert(emp);
    // Includes the newly generated ID value
  }

  private void insertByExample() {
    Employee emp = new Employee();
    // Non-specified columns are excluded from the INSERT clause
    emp.setLastName("Llacolen");
    Employee inserted = this.employeeDAO.insertByExample(emp);
    // Includes the newly generated ID value
  }

  private void updateByPrimayKey() {
    Employee emp = this.employeeDAO.select(134081);
    emp.setSalary(emp.getSalary() + 10);
    int count = this.employeeDAO.update(emp);
  }

  private void updateByExample() {
    Employee example = new Employee();
    example.setBranchId(115);
    Employee values = new Employee();
    values.setSalary(68);
    int count = this.employeeDAO.update(example, values);
  }

  private void updateByCriteria() {
    EmployeeTable e = this.employeeDAO.newTable();
    Employee values = new Employee();
    values.setBranchId(106);
    int count = this.employeeDAO.update(values, e, e.branchId.in(10, 20).or(e.salary.ge(100))).execute();
  }

  private void deleteByPrimaryKey() {
    int count = this.employeeDAO.delete(12);
  }

  private void deleteByExample() {
    Employee example = new Employee();
    example.setBranchId(2);
    int count = this.employeeDAO.delete(example);
  }

  private void deleteByCriteria() {
    EmployeeTable e = this.employeeDAO.newTable();
    int count = this.employeeDAO.delete(e, e.salary.le(70)).execute();
  }

}
