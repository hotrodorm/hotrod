package app;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.metadata.AllColumns.Alias;
import org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase;
import org.hotrod.runtime.livesql.queries.select.SelectFromPhase;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
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
import app.daos.InvoiceVO;
import app.daos.primitives.AbstractInvoiceVO;
import app.daos.primitives.BranchDAO;
import app.daos.primitives.BranchDAO.BranchTable;
import app.daos.primitives.InvoiceDAO;
import app.daos.primitives.InvoiceDAO.InvoiceTable;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

//  @Autowired
//  private NumbersDAO numbersDAO;

  @Autowired
  private BranchDAO branchDAO;

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
//      crud();
//      beans();
//      livesql();
//      join();
      selectByCriteria();
      System.out.println("[ Example complete ]");
    };
  }

//  private void livesql() {
//    reinsert();
//
//    NumbersTable n = NumbersDAO.newTable("n");
//
////    SelectFromPhase<Map<String, Object>> q = this.sql.select().from(n);
//    SelectFromPhase<Map<String, Object>> q = this.sql.select( //
//        n.star() //
//            .filter(c -> c.getName().startsWith("int")) //
//            .as(c -> Alias.literal("zz" + c.getProperty())), //
//        n.int1.minus(n.int2).as("diff")).from(n);
////    SelectFromPhase<Map<String, Object>> q = this.sql.select(n.int1).from(n);
//
//    System.out.println("q:" + q.getPreview());
//    List<Map<String, Object>> rows = q.execute();
//
//    for (Map<String, Object> r : rows) {
//      System.out.println("r=" + r);
//
//      NumbersVO nv = this.numbersDAO.parseRow(r, "zz");
//      System.out.println("nv=" + nv);
//
////      System.out.println(render(r, "int1")); // 5 SMALLINT
////      System.out.println(render(r, "int2")); // 4 INTEGER
////      System.out.println(render(r, "int3")); // -5 BIGINT
////      System.out.println(render(r, "int4")); // 5 SMALLINT
////      System.out.println(render(r, "int5")); // 4 INTEGER
////      System.out.println(render(r, "int6")); // -5 BIGINT
////      System.out.println(render(r, "dec1")); // 2 NUMERIC
////      System.out.println(render(r, "dec2")); // 2 NUMERIC
////      System.out.println(render(r, "dec3")); // 2 NUMERIC
////      System.out.println(render(r, "dec4")); // 2 NUMERIC
////      System.out.println(render(r, "dec5")); // 2 NUMERIC
////      System.out.println(render(r, "dec6")); // 2 NUMERIC
////      System.out.println(render(r, "dec7")); // 2 NUMERIC
////      System.out.println(render(r, "flo1")); // 7 REAL
////      System.out.println(render(r, "flo2")); // 8 DOUBLE
//    }
//
//  }

  private void selectByCriteria() {
//    reinsert();

    InvoiceTable i = InvoiceDAO.newTable("i");
    CriteriaWherePhase<InvoiceVO> q = this.invoiceDAO.selectByCriteria(i, i.branchId.eq(10));
    for (InvoiceVO r : q.execute()) {
      System.out.println("r=" + r);
    }
  }

  private void join() {

    InvoiceTable i = InvoiceDAO.newTable("i");
    BranchTable b = BranchDAO.newTable("c");

    SelectFromPhase<Map<String, Object>> q = this.sql.select( //
        i.star().as(c -> Alias.literal("inx_" + c.getProperty())),
        b.star().as(c -> Alias.literal("brx_" + c.getProperty()))) //
        .from(i).join(b, b.id.eq(i.branchId));
    System.out.println("q:" + q.getPreview());
    List<Map<String, Object>> rows = q.execute();

    for (Map<String, Object> r : rows) {
      System.out.println("r=" + r);
      InvoiceVO inx = this.invoiceDAO.parseRow(r, "inx_");
      System.out.println("inx=" + inx);
      BranchVO brx = this.branchDAO.parseRow(r, "brx_");
      System.out.println("brx=" + brx);
    }

  }

  private String render(final Map<String, Object> r, final String name) {
    Object o = r.get(name);
    if (o == null) {
      return name + ": null";
    } else {
      return name + ": " + o + " (" + o.getClass().getName() + ")";
    }
  }

  private void beans() {
    for (InvoiceVO i : this.invoiceDAO.selectByExample(new AbstractInvoiceVO())) {
      System.out.println("i=" + i + " autowired=" + i.getInvoiceDAO());
    }
    InvoiceVO i10 = this.invoiceDAO.selectByPK(10);
    System.out.println("i10=" + i10 + " autowired=" + i10.getInvoiceDAO());

    InvoiceTable i = InvoiceDAO.newTable("i");
    for (InvoiceVO iv : this.invoiceDAO.selectByCriteria(i, i.id.eq(11)).execute()) {
      System.out.println("iv=" + iv + " autowired=" + iv.getInvoiceDAO());
    }

  }

//  private void crud() {
//    reinsert();
//    for (NumbersVO r : this.numbersDAO.selectByExample(new NumbersVO())) {
//      System.out.println("r=" + r);
//    }
//  }
//
//  private void reinsert() {
//
//    this.numbersDAO.deleteByExample(new AbstractNumbersVO());
//
//    NumbersVO n = new NumbersVO();
//
//    n.setInt1((short) 123);
//    n.setInt2(1234);
//    n.setInt3(123456L);
//    n.setInt4(Integer.valueOf(12345));
//    n.setInt5(-2345);
//    n.setInt6(-45678L);
//
//    n.setColumns(123);
//
//    n.setDec1(BigDecimal.valueOf(1234.56));
//    n.setDec2(BigDecimal.valueOf(7890.12));
//    n.setDec3((byte) 24);
//    n.setDec4((short) 125);
//    n.setDec5(12346);
//    n.setDec6(4455L);
//    n.setDec7(BigInteger.TEN);
//
//    n.setFlo1(123.456f);
//    n.setFlo2(789.012);
//
//    n.setIntTotalAmount(1);
//    n.setDecTotalAmount((short) 1);
//    n.setFloTotalAmount(1.2f);
//
//    this.numbersDAO.insert(n);
//  }

  @MappedJdbcTypes(JdbcType.VARCHAR)
  public class ExampleTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
        throws SQLException {
      ps.setString(i, parameter);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
      return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
      return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
      return cs.getString(columnIndex);
    }
  }

}
