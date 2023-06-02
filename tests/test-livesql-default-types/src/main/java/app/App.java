package app;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.select.SelectFromPhase;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.primitives.BranchDAO;
import app.daos.primitives.BranchDAO.BranchTable;
import app.daos.primitives.InvoiceDAO;
import app.daos.primitives.InvoiceDAO.InvoiceTable;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@MapperScan("mappers")
public class App {
//
//  @Autowired
//  private NumbersDAO numbersDAO;
//
//  @Autowired
//  private InvoiceDAO invoiceDAO;
//
//  @Autowired
//  private BranchDAO branchDAO;

//  @Autowired
//  private TorcsMetrics metrics;

  @Autowired
  private LiveSQL sql;

//  @Autowired
//  private TorcsCTP torcsCTP;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
//      crud();
//      join();
//      join();
      livesql();
//      selectByCriteria();
//      torcs();
//      star();
//      noFrom();
      System.out.println("[ Example complete ]");
    };
  }

  private void livesql() {
    for (Row r : this.sql.select(sql.val("abc").ascii().as("code")).execute()) {
      System.out.println("r=" + r);
    }
//    for (Row r : this.sql.select(sql.val("abc").ascii().as("code")).execute()) {
//      System.out.println("r=" + r);
//    }
  }

//  private void livesql() {
////    reinsert();
//
//    NumbersTable n = NumbersDAO.newTable("n");
//
////    SelectFromPhase<Row> q = this.sql.select().from(n);
//    SelectFromPhase<Row> q = this.sql
//        .select(n.star().filter(c -> c.getName().startsWith("INT")), n.num1.minus(n.num2).as("mainDiffLatest")).from(n);
////    SelectFromPhase<Row> q = this.sql.select(n.int1).from(n);
//
//    System.out.println("q:" + q.getPreview());
//    List<Row> rows = q.execute();
//
//    for (Map<String, Object> r : rows) {
//      System.out.println("r=" + r);
//
//      NumbersVO nv = this.numbersDAO.parseRow(r);
//
//      System.out.println(render(r, "int1")); // 5 SMALLINT
//      System.out.println(render(r, "int2")); // 4 INTEGER
//      System.out.println(render(r, "int3")); // -5 BIGINT
//      System.out.println(render(r, "int4")); // 5 SMALLINT
//      System.out.println(render(r, "int5")); // 4 INTEGER
//      System.out.println(render(r, "int6")); // -5 BIGINT
//      System.out.println(render(r, "dec1")); // 2 NUMERIC
//      System.out.println(render(r, "dec2")); // 2 NUMERIC
//      System.out.println(render(r, "dec3")); // 2 NUMERIC
//      System.out.println(render(r, "dec4")); // 2 NUMERIC
//      System.out.println(render(r, "dec5")); // 2 NUMERIC
//      System.out.println(render(r, "dec6")); // 2 NUMERIC
//      System.out.println(render(r, "dec7")); // 2 NUMERIC
//      System.out.println(render(r, "flo1")); // 7 REAL
//      System.out.println(render(r, "flo2")); // 8 DOUBLE
//    }
//
//  }

  private String render(final Map<String, Object> r, final String name) {
    Object o = r.get(name);
    if (o == null) {
      return name + ": null";
    } else {
      return name + ": " + o + " (" + o.getClass().getName() + ")";
    }
  }

//  private void crud() {
////    reinsert();
//    for (NumbersVO r : this.numbersDAO.select(new NumbersVO())) {
//      System.out.println("r=" + r
////          + " dao=" + r.getNumbersDAO()
//      );
//    }
//  }

  private void star() {

    InvoiceTable i = InvoiceDAO.newTable("i");
    BranchTable b = BranchDAO.newTable("c");

    SelectFromPhase<Row> q = this.sql.select( //
        i.id, i.amount.as("totalAmount"), // declared columns w/wo alias
        i.star(), // simple *
        b.star().filter(c -> true), // filtered *
        b.star().as(c -> "b#" + c.getProperty()) // aliased *
    ) //
        .from(i) //
        .join(b, b.id.eq(i.branchId));
    System.out.println("q:" + q.getPreview());
    List<Row> rows = q.execute();
    for (Row r : rows) {
      System.out.println("r=" + r);
    }

  }

//  private void selectByCriteria() {
////    reinsert();
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    CriteriaWherePhase<InvoiceVO> q = this.invoiceDAO.select(i, i.branchId.eq(10));
//    System.out.println("q=" + q.getPreview());
//    for (InvoiceVO r : q.execute()) {
//      System.out.println("r=" + r);
//    }
//  }

//  private void join() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    ExecutableSelect<Row> q = this.sql.select( //
//        i.star().as(c -> "in#" + c.getProperty()), //
//        b.star().as(c -> "br#" + c.getProperty()) //
//    ) //
//        .from(i) //
//        .join(b, b.id.eq(i.branchId)) //
//        .where(b.name.like("%"));
//    System.out.println("q:" + q.getPreview());
//    List<Row> rows = q.execute();
//
//    for (Row r : rows) {
//      InvoiceVO inx = this.invoiceDAO.parseRow(r, "in#");
//      BranchVO brx = this.branchDAO.parseRow(r, "br#");
//
//      System.out.println("r=" + r);
//      System.out.println("inx=" + inx
////          + " dao=" + inx.getInvoiceDAO()
//      );
//      System.out.println("brx=" + brx);
//    }
//
//  }

//  private void torcs() {
//
//    System.out.println("--- Torcs --------------------------------------------------------");
//    int pos = 1;
//    for (Statement m : this.metrics.getByHighestResponseTime()) {
//      System.out.println("#" + pos + ": " + m.toString());
//      pos++;
//    }
//
////    System.out.println("--- Torcs PLAN ---------------------------------------------------");
////    Statement h = this.sqlMetrics.getByHighestAvgResponseTime().get(0);
////    String plan = this.torcsCTP.getEstimatedCTPExecutionPlan(h);
////    System.out.println("Plan: " + plan);
//
//  }

  private void noFrom() {
    System.out.println("--- No FROM #1 ---------------------------------------------------");
    for (Row r : this.sql.select(sql.val(3).mult(7).as("result"), sql.currentDateTime().as("now")).execute()) {
      System.out.println("r=" + r);
    }

//    System.out.println("--- No FROM #2 ---------------------------------------------------");
//    for (Row r : this.sql.select(sql.val(3).mult(7).as("result"), sql.currentDateTime().as("now")).from(sql.DUAL)
//        .execute()) {
//      System.out.println("r=" + r);
//    }
  }

//  private void reinsert() {
//
//    this.numbersDAO.delete(new AbstractNumbersVO());
//
//    NumbersVO n = new NumbersVO();
//
//    n.setId(1);
//
//    n.setNum1((byte) 123);
//    n.setNum2((short) 456);
//    n.setNum3(789);
//
////    n.setInt1(123);
////    n.setInt2(456);
////    n.setInt3(789);
//
////    n.setInt2(1234);
////    n.setInt3(123456L);
////    n.setInt4(Integer.valueOf(12345));
////    n.setInt5(-2345);
////    n.setInt6(-45678L);
////
////    n.setColumns(123);
////
////    n.setDec1(BigDecimal.valueOf(1234.56));
////    n.setDec2(BigDecimal.valueOf(7890.12));
////    n.setDec3((byte) 24);
////    n.setDec4((short) 125);
////    n.setDec5(12346);
////    n.setDec6(4455L);
////    n.setDec7(BigInteger.TEN);
////
////    n.setFlo1(123.456f);
////    n.setFlo2(789.012);
////
////    n.setIntTotalAmount(1);
////    n.setDecTotalAmount((short) 1);
////    n.setFloTotalAmount(1.2f);
//
//    this.numbersDAO.insert(n);
//  }

}
