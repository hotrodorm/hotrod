package app;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import app.daos.NumMODEL;
import app.daos.primitives.PrimeDAO;
import app.daos.primitives.PrimeVO;
import app.daos.primitives.Queries2DAO;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

//  @Autowired
//  private QueriesDAO queriesDAO;

  @Autowired
  private Queries2DAO queries2DAO;

//  @Autowired
//  private EmployeeDAO employeeDAO;

  @Autowired
  private PrimeDAO primeDAO;

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

    PrimeVO p = this.primeDAO.selectByPK(1);
    System.out.println("p=" + p);

    List<NumMODEL> nums = this.queries2DAO.findNumbers();
//    System.out.println("nums=" + nums.get  .stream().map(n -> "" + n).collect(Collectors.joining("|")));
    for (NumMODEL nm : nums) {
      System.out
          .println("nm=" + nm + " -- " + Stream.of(nm.getNmbs()).map(n -> "" + n).collect(Collectors.joining("|")));
    }

//    System.out.println("-- p[" + p.getId() + "], n=" + p.getN() + " (" + p.getN().getClass().getName() + ")");
//    PgArray a = (PgArray) p.getN();
//
//    try {
//      Object o = a.getArray();
//      System.out.println("o: " + o.getClass().getName());
//      Integer[] il = (Integer[]) a.getArray();
//
//      System.out.println("il[" + il.length + "]: " + Stream.of(il).map(n -> "" + n).collect(Collectors.joining(", ")));
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }

//    List<PrimesMODEL> ps = this.queries2DAO.findPrimes();
//    for (PrimesMODEL p : ps) {
//      System.out.println("p=" + p);
//    }
//    System.out.println("-- total rows=" + ps.size());

//    List<Short> ids = Arrays.asList((short)101, (short)102, (short)200);
//    List<String> names = Arrays.asList("Alice", "Steve");
//
//    List<EVO> evos = this.queriesDAO.findEmployees(ids, names);
//    System.out.println("EVOs:");
//    for (EVO evo : evos) {
//      System.out.println(evo);
//    }

  }

}
