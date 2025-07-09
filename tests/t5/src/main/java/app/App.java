package app;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.queries.select.Select;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.AccountDAO;
import app.persistence.dao.AccountDAO.AccountTable;
import app.persistence.dao.ProductDAO;

@SpringBootApplication
@Configuration
public class App {

  private static final Logger log = Logger.getLogger(App.class.getName());

  static {
//    JULCustomFormatter.initialize(Level.FINER);
  }

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private ProductDAO productDAO;

//  @Autowired
//  private ADAO aDAO;

//  @Autowired
//  private ReportingDAO reportingDAO;

//  @Autowired
//  private Test2DAO test2DAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      log.info("[ Starting... ]");
//      test();
//      testLiveSQL();
      testLiveSQLUnary();
//      testLiveSQLTuples();
//      testLiveSQLCursor();
//      testConverter5();
//      testConverter6();
//      testOptimisticLocking();
//      testNitro6();
      log.info("[ Ending ]");
    };
  }

//  private void testOptimisticLocking() throws SQLException, DynamicExpressionException {
////    testOLInsert();
////  testOLInsertByExample() ;
////    testOLUpdate();
////    testOLDelete();
////  testOLDeleteDelete();
////    testOLDeleteUpdate();
//    testOLUpdateDelete();
////    testOLUpdateUpdate();
//  }

//  private void testOLInsert() throws DynamicExpressionException, SQLException {
//    Account a = new Account();
//    a.setName("1010-4");
//    a.setType(AccountType.CHK);
//    a.setBalance(100);
////    a.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
////    a.setActive(true);
////    a.setVersion(1);
//    this.accountDAO.insert(a);
//    System.out.println("--> a=" + a);
//
//    Account b = this.accountDAO.select(a.getId());
//    System.out.println("b=" + b);
//  }
//
//  private void testOLInsertByExample() throws DynamicExpressionException, SQLException {
//    Account a = new Account();
//    a.setName("1010-4");
//    a.setType(AccountType.CHK);
//    a.setBalance(100);
////    a.setActive(true);
////    a.setVersion(1);
//    this.accountDAO.insertByExample(a);
//    System.out.println("--> a=" + a);
//
//    Account b = this.accountDAO.select(a.getId());
//    System.out.println("b=" + b);
//  }
//
//  private void testOLUpdate() throws DynamicExpressionException, SQLException {
//    Account a = this.accountDAO.select(112);
//    System.out.println("--> a=" + a);
//    AccountBaseline baseline = this.accountDAO.baseline(a);
//    a.setBalance(a.getBalance() + 100);
////    this.accountDAO.update(a, baseline);
//    System.out.println("Updated a.");
//  }
//
//  private void testOLDelete() throws DynamicExpressionException, SQLException {
//    Account a = this.accountDAO.select(112);
//    AccountBaseline baseline = this.accountDAO.baseline(a);
//    System.out.println("--> a=" + a);
////    this.accountDAO.delete(baseline);
//    System.out.println("Updated a.");
//  }
//
//  private void testOLDeleteUpdate() throws DynamicExpressionException, SQLException {
//    Account b = this.accountDAO.select(112);
//
//    Account a = this.accountDAO.select(112);
//    System.out.println("--> a=" + a);
//    AccountBaseline baseline = this.accountDAO.baseline(a);
//
//    this.accountDAO.delete(b);
//    System.out.println("Deleted b.");
//
//    a.setBalance(a.getBalance() + 100);
////    this.accountDAO.update(a, baseline);
//    System.out.println("Updated a.");
//  }
//
//  private void testOLUpdateDelete() throws DynamicExpressionException, SQLException {
//    Account b = this.accountDAO.select(112);
//
//    Account a = this.accountDAO.select(112);
//    System.out.println("--> a=" + a);
//    AccountBaseline baseline = this.accountDAO.baseline(a);
//
//    a.setBalance(a.getBalance() + 100);
////    this.accountDAO.update(a, baseline);
//    System.out.println("Updated a.");
//
//    this.accountDAO.delete(b);
//    System.out.println("Deleted b.");
//  }

//  private void testNitro6() throws SQLException, DynamicExpressionException, IOException {
//    List<TAccount> tas = this.test2DAO.findTAccounts(false);
//    for (TAccount ta : tas) {
//      System.out.println("ta=" + ta);
//
//    }
//  }

//  private void testConverter6() throws SQLException, DynamicExpressionException, IOException {
//
//    AccountTable a = this.accountDAO.newTable();
//
////    TypeConverter<Integer, Boolean> converter = new IntegerBooleanConverter();
////    {
////      TypeHandler<Integer, Boolean> th = TypeHandler.forConverter(new IntegerBooleanConverter(),
////          TypeSource.ENTITY_COLUMN);
////      ConvertedColumn<Integer, Boolean> dactive = new ConvertedColumn<Integer, Boolean>(a, "ACTIVE", "active",
////          "INTEGER", 32, 0, th, th.getConverter());
////    }
////    Integer th = 12;
//
////    TypeHandler th2 = TypeHandler.of(AccountTypeConverter.class, TypeSource.ENTITY_COLUMN);
////    @SuppressWarnings("unchecked")
////    TypeConverter<String, AccountType> converter2 = (TypeConverter<String, AccountType>) th2.getConverter();
////    ConvertedColumn<String, AccountType> dtype = new ConvertedColumn<String, AccountType>(a, "TYPE", "type", "VARCHAR",
////        3, 0, TypeHandler.of(IntegerBooleanConverter.class, TypeSource.ENTITY_COLUMN), converter2);
//
////    SelectWherePhase<Row> q = this.sql.select().from(a).where(dactive.eq(true));
////    SelectWherePhase<Row> q = this.sql.select().from(a).where(dtype.ne(AccountType.CHK));
//    Select<Row> q = this.sql.select(a.balance.as("bal"), a.active.as("dac"), //
//        a.active.coalesce(true).as("coa"), a.type.nullIf(AccountType.PEN).as("tnull")) //
//        .from(a).where(a.type.notIn(AccountType.CHK, AccountType.PEN).and(a.active.eq(true))).orderBy(a.balance);
////    Select<Row> q = this.sql.select().from(a).where(dtype.notIn(AccountType.CHK, AccountType.INV)).orderBy(a.balance);
//    System.out.println("query:\n" + q.getPreview(true));
//    List<Row> rows = q.execute();
//
//    for (Row r : rows) {
//      System.out.println("Row=" + r);
//    }
//  }
//
////  public class OneConvertedColumn<R, D> extends ConvertedColumn<R, D> {
////
////    private String property;
////    private String type;
////    private Integer columnSize;
////    private Integer decimalDigits;
////
////    public OneConvertedColumn(final TableOrView objectInstance, final String name, final String property,
////        final String type, final Integer columnSize, final Integer decimalDigits, final TypeHandler handler,
////        final TypeConverter<R, D> converter) {
////      super(Expression.PRECEDENCE_COLUMN, objectInstance, name, converter);
////      this.property = property;
////      this.type = type;
////      this.columnSize = columnSize;
////      this.decimalDigits = decimalDigits;
////      super.setTypeHandler(handler);
////    }
////
////  }
//
////  private void testConverter5() throws SQLException, DynamicExpressionException, IOException {
////    ATable a = this.aDAO.newTable();
////
////    System.out.println(">> query 1");
////
////    Select<Row> q = this.sql.select(a.star()).from(a)
//////        .where(a.esActivo.eq(true))
////    ;
////    System.out.println(">> q:" + q.getPreview());
////
////    List<Row> lista = q.execute();
////
////    System.out.println(">> A:");
////    lista.forEach(r -> System.out.println(r));
////
////  }
//
//  private void testLiveSQLCursor() throws SQLException, DynamicExpressionException, IOException {
//
////    try (Cursor<Row> rows = this.sql.select(sql.val(7).mult(3).as("answer"), sql.ONE.as("one"))
////        .executeCursor(60);) {
////      System.out.println(">> Rows:");
////      rows.forEach(r -> System.out.println(r));
////    }
//
//    AccountTable a = this.accountDAO.newTable();
//    try (Cursor<Account> accounts = this.accountDAO.select(a, sql.TRUE).executeCursor(60);) {
//      System.out.println(">> Accounts:");
//      accounts.forEach(r -> System.out.println(r));
//    }
//
//  }

  private void testLiveSQLUnary() throws SQLException, DynamicExpressionException {

    AccountTable a = this.accountDAO.newTable();
    AccountTable a2 = this.accountDAO.newTable();

//    Subquery x = sql.subquery("x", sql.select(sql.val(100).mult(1.1).as("cost"), a.balance).from(a).limit(1));

//    Subquery x = sql.subquery("x", sql.select(a.balance.as("balx")).from(a).limit(1));
//    Subquery y = sql.subquery("y", sql.select(x.num("balx").as("baly")).from(x).limit(1));

////    Subquery x = sql.subquery("x", sql.select(a.star().filter(c->c.getProperty().equals("balance") ||c.getProperty().equals("name"))).from(a).limit(1));
//    Subquery x = sql.subquery("x", sql.select(a.star().filter(c->c.getProperty().equals("balance") )).from(a).limit(1));
//    Subquery y = sql.subquery("y", sql.select(x.num("balance").as("baly")).from(x).limit(1));
//    List<Row> rows = sql.select(y.num("baly").as("balm")) //
////    List<Row> rows = sql.select(x.num("cost"), x.num("balance")) //
//        .from(y) //
//        .execute();

//  Subquery x = sql.subquery("x", sql.select(a.star().filter(c->c.getProperty().equals("balance") ||c.getProperty().equals("name"))).from(a).limit(1));
//    Subquery x = sql.subquery("x", sql.select(a.star().filter(c->c.getProperty().equals("balance") )).from(a).limit(1));

//    Subquery x = sql.subquery("x", sql.select(a.id, a.name, a.updatedAt).from(a).limit(1));
    Subquery x = sql.subquery("x", sql.select(a.balance.as("balx"), a.name, a.updatedAt).from(a).limit(1));

    Subquery y = sql.subquery("y",
        sql.select(x.num("balx").as("baly"), x.str("name"), x.dt("updatedAt")).from(x).limit(1));
    Select<Row> q = sql.select(y.num("baly").as("balm"), y.str("name").as("namy"), y.dt("updatedAt").as("updated"),
        sql.literal(123).mult(3).as("total")) //
//  List<Row> rows = sql.select(x.num("cost"), x.num("balance")) //
        .from(y);
//    System.out.println("=== QUERY ===\n" + q.getPreview(true));
    List<Row> rows = q.execute();

//    List<Row> rows = sql.select(sql.literal(123).mult(3).as("total")).execute();
//    List<Row> rows = sql.select(sql.literal(123).mult(3).as("total")).execute();

//    Subquery x = sql.subquery("x", sql.select(sql.val(100).mult(1.1).as("cost"), a2.balance).from(a2).limit(1));
//
//    List<Row> rows = sql.select( //
//
//        sql.val(3).mult(7).as("m1"), //
//        a.id, //
//        x.num("cost"), //
//        x.num("balance"), //
//        //
//        sql.val(3).mult(7).as("multi"), //
//        a.id.as("bid"), //
//        x.num("cost").as("total") //
//
//    ) //
//        .from(a) //
//        .crossJoin(x) //
//        .execute();

    for (Row r : rows) {
      System.out.println("r=" + r);
    }

  }

//  private void testLiveSQLTuples() throws SQLException, DynamicExpressionException {
//    AccountTable a = this.accountDAO.newTable();
//    ProductTable p = this.productDAO.newTable();
//
//    List<Tuple2<Account, Product>> rows = this.sql.selectTuples().from(a).crossJoin(p).execute();
//    for (Tuple2<Account, Product> r : rows) {
//      System.out.println("- Account: " + r.getA());
//      System.out.println("- Product: " + r.getB());
//    }
//  }

  private void testLiveSQL() throws SQLException, DynamicExpressionException {

//    Account ax = this.accountDAO.select(123);
//    System.out.println(">> Account 123: " + ax);

//    AccountTable a = this.accountDAO.newTable();
//    List<Account> accounts = this.accountDAO.select(a, a.balance.gt(500)).execute();
//    System.out.println(">> Accounts:");
//    accounts.forEach(r -> System.out.println(r));

//    Select<Row> s = this.sql.select(sql.val(7).mult(3).as("answer"));
//    System.out.println("Query:" + s.getPreview());
//    Row row = s.executeOne();
//    System.out.println("Row=" + row);

//    System.out.println("Will UPDATE by criteria.");
//    AccountTable a = this.accountDAO.newTable();
//    Account updateValues = new Account();
//    updateValues.setBalance(777);
//    int count = this.accountDAO.update(updateValues, a, a.balance.lt(150)).execute();
//    System.out.println("UPDATE by criteria complete: count=" + count);
//
//    List<Account> accounts = this.accountDAO.select(a, sql.TRUE).execute();
//    accounts.forEach(r -> System.out.println("r=" + r));

//    System.out.println("Will select by criteria." );
//    AccountTable a = this.accountDAO.newTable();
//    List<Account> accounts = this.accountDAO.select(a, a.type.eq("CHK")).orderBy(a.balance.desc()).execute();
//    for (Account r : accounts) {
//      System.out.println("account=" + r);
//    }
//    System.out.println("Select by criteria complete." );

//    System.out.println("Will DELETE by criteria.");
//    AccountTable a = this.accountDAO.newTable();
//    int count = this.accountDAO.delete(a, a.type.eq("SAV")).execute();
//    System.out.println("DELETE by criteria complete: count=" + count);

//    List<Row> rows = this.sql.select().from(a).execute();
//    for (Row r : rows) {
//      System.out.println("Row=" + r);
//    }
  }

  private void test() throws SQLException, DynamicExpressionException {

//    Account a = this.accountDAO.select(112);
//    System.out.println("--> a=" + a);

//    List<Integer> ids = Arrays.asList(123, 789, 112, 4);

//    int rows = this.reportingDAO.activateBigAccounts(125L, ids);
//      int rows=  this.reportingDAO.activateBigAccounts2(125L, ids, "type = 'CHK'");

    // List<BigAccount> bas = this.reportingDAO.findBigAccounts();
//    for (BigAccount ba : bas) {
//      System.out.println("--> " + ba);
//    }

//    List<Account> bas = this.accountDAO.findBigAccounts();
//    for (Account ba : bas) {
//      System.out.println("--> " + ba);
//    }

    // int rows = this.accountDAO.activateBigAccounts(null);
//    System.out.println("--> rows=" + rows);

//    Account filter = new Account();
//    filter.setType("CHK");
//
//    List<Account> accounts = this.accountDAO.select(filter, AccountOrderBy.ID, AccountOrderBy.NAME$DESC);
//    for (Account a : accounts) {
//      System.out.println("--> " + a);
//    }

//  List<Account> accounts = this.accountDAO.select1();
//  for (Account a : accounts) {
//    System.out.println("--> " + a);
//  }

//    Account a = new Account();
//    a.setId(400);
//    a.setName("ACC123");
//    a.setType(null);
//    a.setBalance(true);
//
//    this.accountDAO.insert(a);
//    System.out.println("> Account inserted -- id=" + a.getId());
//
//    Account filter = new Account();
//    filter.setType("PEN");
//    int cnt = this.accountDAO.delete(filter);
//    System.out.println("> Accounts deleted: " + cnt);

  }

}
