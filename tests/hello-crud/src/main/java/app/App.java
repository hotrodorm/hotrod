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
    Employee emp = this.employeeDAO.select(150);
    System.out.println("1. SELECT BY PK - employee=" + emp);
  }

  private void selectByExample() {
    Employee example = new Employee();
    example.setBranchId(2);
    // Selecting by example without ordering
    List<Employee> employees = this.employeeDAO.select(example);
    employees.forEach(e -> System.out.println("2a. SELECT BY EXAMPLE - employee=" + e));
    // You can add simple sorting criteria
    List<Employee> employeesb = this.employeeDAO.select(example, EmployeeOrderBy.LAST_NAME$DESC,
        EmployeeOrderBy.FIRST_NAME);
    employeesb.forEach(e -> System.out.println("2b. SELECT BY EXAMPLE - employee=" + e));
  }

  private void selectByCriteria() {
    EmployeeTable e = this.employeeDAO.newTable();
    // Using a complex search criteria
    List<Employee> employees = this.employeeDAO.select(e, //
        e.salary.between(70, 100).and(e.lastName.like("%t%")) //
    ).execute();
    employees.forEach(y -> System.out.println("3a. SELECT BY CRITERIA - employee=" + y));
    // Adding ORDER BY, OFFSET, LIMIT, FOR UPDATE, SKIP LOCKED
    List<Employee> employees2 = this.employeeDAO.select(e, //
        e.salary.between(70, 100).and(e.lastName.like("%t%")) //
    ) //
        .orderBy(sql.caseWhen(e.branchId.between(2, 4), 0).elseValue(1).end(), e.salary.desc()) //
        .offset(1) //
        .limit(1) //
        .forUpdate() //
        .skipLocked() //
        .execute();
    employees2.forEach(y -> System.out.println("3b. SELECT BY CRITERIA - employee=" + y));
  }

  private void insert() {
    Employee emp = new Employee();
    // Auto-generated ID, blank salary, blank branch_id
    emp.setFirstName("Catherine");
    emp.setLastName("De Bourgh");
    emp.setSalary(140);
    Employee inserted = this.employeeDAO.insert(emp);
    // Includes the newly generated ID value
    System.out.println("4. INSERT - employee=" + inserted);
  }

  private void insertByExample() {
    Employee emp = new Employee();
    // Non-specified columns are excluded from the INSERT clause
    emp.setLastName("Llacolen");
    Employee inserted = this.employeeDAO.insertByExample(emp);
    // Includes the newly generated ID value
    System.out.println("5. INSERT BY EXAMPLE - employee=" + inserted);
  }

  private void updateByPrimayKey() {
    Employee emp = this.employeeDAO.select(154);
    emp.setSalary(emp.getSalary() + 10);
    int count = this.employeeDAO.update(emp);
    System.out.println("6. UPDATE BY PRIMARY KEY - count=" + count);
  }

  private void updateByExample() {
    Employee example = new Employee();
    example.setBranchId(4);
    Employee values = new Employee();
    values.setSalary(78);
    int count = this.employeeDAO.update(example, values);
    System.out.println("7. UPDATE BY EXAMPLE - count=" + count);
  }

  private void updateByCriteria() {
    EmployeeTable e = this.employeeDAO.newTable();
    Employee values = new Employee();
    values.setBranchId(106);
    int count = this.employeeDAO.update(values, e, //
        e.branchId.in(6, 16).or(e.salary.ge(105)) //
    ).execute();
    System.out.println("8. UPDATE BY CRITERIA - count=" + count);
  }

  private void deleteByPrimaryKey() {
    int count = this.employeeDAO.delete(154);
    System.out.println("9. DELETE BY PRIMARY KEY - count=" + count);
  }

  private void deleteByExample() {
    Employee example = new Employee();
    example.setBranchId(7);
    int count = this.employeeDAO.delete(example);
    System.out.println("10. DELETE BY EXAMPLE - count=" + count);
  }

  private void deleteByCriteria() {
    EmployeeTable e = this.employeeDAO.newTable();
    int count = this.employeeDAO.delete(e, //
        e.salary.le(70).or(e.lastName.like("G%")) //
    ).execute();
    System.out.println("11. DELETE BY CRITERIA - count=" + count);
  }

}
