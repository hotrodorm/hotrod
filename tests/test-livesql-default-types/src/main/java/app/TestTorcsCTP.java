package app;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
import org.hotrod.torcs.Torcs;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

//@Configuration
//@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@MapperScan("mappers")
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
@PropertySource(value = { "file:application.properties",
    "classpath:application.properties" }, ignoreResourceNotFound = true)
public class TestTorcsCTP {

  public static void main(String[] args) {
    SpringApplication.run(TestTorcsCTP.class, args);
  }

  @Autowired
  private LiveSQL sql;

  @Autowired
  private Torcs torcs;

//  @Autowired
//  private TorcsCTP torcsCTP;
//
//  @Autowired
//  private DataSource ds;
//
//  @Bean
//  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//    return args -> {
//      System.out.println("[ Torcs Starting example ]");
//
////      this.executeSampleSql();
//      this.executeSelect();
////      this.executeSelect2();
//
//      System.out.println("-- Ranking ---");
//      for (RankingEntry re : this.torcs.getDefaultRanking().getEntries()) {
//        System.out.println("SQL:\n" + re.getSQL() + "\nCTP Plan:");
//        this.torcsCTP.getEstimatedCTPExecutionPlan(re.getSlowestExecution()).stream()
//            .forEach(l -> System.out.println(l));
//      }
//      System.out.println("-- End of Ranking ---");
//
//      System.out.println("[ Torcs Example complete ]");
//    };
//  }
//
//  private void executeSampleSql() throws SQLException {
//    Connection conn = ds.getConnection();
//    Statement st = conn.createStatement();
//    ResultSet rs = st.executeQuery("select 3 * 7");
//    while (rs.next()) {
//      System.out.println("line: " + rs.getString(1));
//    }
//  }
//
//  private void executeSelect() {
//    this.sql.select(sql.literal(3).mult(7)).execute();
//  }
//
//  private void executeSelect2() {
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//    InvoiceTable j = InvoiceDAO.newTable("j");
//    InvoiceTable k = InvoiceDAO.newTable("k");
//
//    this.sql.select() //
//        .from(i) //
//        .leftJoin(b, b.id.eq(i.branchId)) //
//        .where(i.branchId.notIn( //
//            this.sql.select(sql.min(j.branchId)).from(j) //
//                .where(j.type.ne("VIP")
//                    .and(j.amount.gtAny(this.sql.select(k.amount).from(k)
//                        .where(k.accountId.ne(i.accountId).and(k.accountId.ne(j.accountId)))))))
//            .or(i.amount.ge(b.id.mult(1000).minus(b.id)))) //
//        .execute();
//  }

}
