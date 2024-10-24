package app;

import java.util.Arrays;
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

import app.daos.BranchVO;
import app.daos.primitives.BranchDAO;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

//  @Autowired
//  private QueriesDAO queriesDAO;
//
//  @Autowired
//  private EmployeeDAO employeeDAO;

//  @Autowired
//  private PlayerDAO playerDAO;
//
//  @Autowired  
//  private Car_part_priceDAO cppDAO;

  @Autowired
  private BranchDAO branchDAO;

//  @Autowired
//  private QueriesBDAO queriesBDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
//      converterPGArray();
//      updateByCriteria();
//      deleteByCriteria();
      selectEntity();
//      converter();
//      forEach();
      System.out.println("[ Example complete ]");
    };
  }

  private void selectEntity() {

    BranchVO b = this.branchDAO.select(102);
    System.out.println("> b=" + b);

//    List<BranchVO> branches = this.branchDAO.findTop3Branches();
//    System.out.println("> Top 3 branches: ");
//    for (BranchVO b : branches) {
//      System.out.println("> b=" + b);
//    }
//
//    List<LowBranchVO> lowBranches = this.queriesBDAO.findLowBranches();
//    System.out.println("> Low branches: ");
//    for (LowBranchVO lb : lowBranches) {
//      System.out.println("> lb=" + lb);
//    }

  }

//  private void converterPGArray() {
//    PlayerVO p = this.playerDAO.select(101);
//    System.out.println("> Player:" + p.getId() + " cards="
//        + Stream.of(p.getCards()).map(c -> "" + c).collect(Collectors.joining("|")));
//    p.setCards(new Integer[] { 7, 8, 8, 9 });
//    this.playerDAO.update(p);
//    System.out.println("> Player updated.");
//  }
//
//  private void updateByCriteria() {
//    System.out.println("> updateByCriteria()");
//    Car_part_priceTable p = Car_part_priceDAO.newTable("p");
//
//    Car_part_priceVO updateValues = new Car_part_priceVO();
//    updateValues.set_discount(250);
//    updateValues.setPrice_dollar(150);
//    UpdateSetCompletePhase u = this.cppDAO.update(updateValues, p, p.part_.eq(1));
//    System.out.println("Update SQL:" + u.getPreview());
//    u.execute();
//
//    for (Car_part_priceVO vo : this.cppDAO.select(p, p.part_.eq(p.part_)).execute()) {
//      System.out.println("> CPP=" + vo);
//    }
//
//  }
//
//  private void deleteByCriteria() {
//    System.out.println("> deleteByCriteria()");
//    Car_part_priceTable p = Car_part_priceDAO.newTable("p");
//
//    Car_part_priceVO updateValues = new Car_part_priceVO();
//    updateValues.set_discount(250);
//    updateValues.setPrice_dollar(150);
//    DeleteWherePhase d = this.cppDAO.delete(p, p.part_.eq(2));
//    System.out.println("Delete SQL:" + d.getPreview());
//    d.execute();
//
//    CriteriaWherePhase<Car_part_priceVO> s2 = this.cppDAO.select(p, p.part_.eq(p.part_));
//    System.out.println("Select SQL:" + s2.getPreview());
//    for (Car_part_priceVO vo : s2.execute()) {
//      System.out.println("> CPP=" + vo);
//    }
//
//  }

  private void converter() {
//    System.out.println("Employee:" + this.employeeDAO.select(123));
  }

  private void forEach() {

    List<Integer> ids = Arrays.asList(101, 102, 200);
    List<String> names = Arrays.asList("Alice", "Steve");

//    List<EVO> evos = this.queriesDAO.findEmployees(ids, names);
//    System.out.println("EVOs:");
//    for (EVO evo : evos) {
//      System.out.println(evo);
//    }

  }

}
