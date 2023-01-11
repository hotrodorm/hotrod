package test;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import test.persistence.TypesBinaryVO;
import test.persistence.TypesCharVO;
import test.persistence.TypesNumericVO;
import test.persistence.primitives.EmployeeDAO;
import test.persistence.primitives.EmployeeDAO.EmployeeTable;
import test.persistence.primitives.TypesBinaryDAO;
import test.persistence.primitives.TypesCharDAO;
import test.persistence.primitives.TypesDateTimeDAO;
import test.persistence.primitives.TypesNumericDAO;
import test.persistence.primitives.TypesOtherDAO;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private TypesNumericDAO typesNumericDAO;

  @Autowired
  private TypesCharDAO typesCharDAO;

  @Autowired
  private TypesBinaryDAO typesBinaryDAO;

  @Autowired
  private TypesDateTimeDAO typesDateTimeDAO;

  @Autowired
  private TypesOtherDAO typesOtherDAO;

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

    List<Map<String, Object>> l = this.sql.select().from(e).where(e.name.like("A%")).execute();

    System.out.println("Employees with names that start with 'A':");
    for (Map<String, Object> r : l) {
      System.out.println(r);
    }

    // Inserting in types_numeric

    TypesNumericVO tn = new TypesNumericVO();

    tn.setInt1((short) 123);
    tn.setInt2(1234);
    tn.setInt3(123456L);
    tn.setInt4(new Integer(12345));
    tn.setInt5(-2345);
    tn.setInt6(-45678L);

    tn.setDec1(BigDecimal.TEN);
    tn.setDec2(BigDecimal.TEN);
    tn.setDec3((byte) 24);
    tn.setDec4((short) 125);
    tn.setDec5(12346);
    tn.setDec6(4455L);
    tn.setDec7(BigInteger.TEN);

    tn.setFlo1(123.456f);
    tn.setFlo2(789.012);

    this.typesNumericDAO.insert(tn);

    for (TypesNumericVO n : this.typesNumericDAO.selectByExample(new TypesNumericVO())) {
      System.out.println("n=" + n);
    }

    // Inserting in types_char

    TypesCharVO tc = new TypesCharVO();

    tc.setCha1("Hello");
    tc.setCha2("World");
    tc.setCha3("Fun");

    this.typesCharDAO.insert(tc);

    for (TypesCharVO c : this.typesCharDAO.selectByExample(new TypesCharVO())) {
      System.out.println("c=" + c);
    }

    // Inserting in types_binary

    TypesBinaryVO tb = new TypesBinaryVO();

    byte[] v = new byte[] { 12, 34, 56, 78 };
    tb.setBin1(v);
    tb.setBol1(true);

    this.typesBinaryDAO.insert(tb);

    for (TypesBinaryVO c : this.typesBinaryDAO.selectByExample(new TypesBinaryVO())) {
      System.out.println("b=" + tb);
      System.out.println("bin1=" + tb.getBin1()[0] + tb.getBin1()[1] + tb.getBin1()[2] + tb.getBin1()[3]);
    }

  }

}
