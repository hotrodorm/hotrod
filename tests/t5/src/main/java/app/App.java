package app;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.ProductDAO;
import app.persistence.dao.ProductDAO.ProductTable;
import app.persistence.dao.SalesDAO;
import app.persistence.dao.VehicleDAO;
import app.persistence.layout.VehicleLayout;
import app.persistence.model.MyAccount;
import app.persistence.model.Product;
import app.persistence.model.Vehicle;

@SpringBootApplication
@Configuration
public class App {

  private static final Logger log = Logger.getLogger(App.class.getName());

  static {
//    JULCustomFormatter.initialize(Level.FINER);
  }

//  @Autowired
//  private AccountDAO accountDAO;
//
//  @Autowired
//  private CoinDAO coinDAO;

  @Autowired
  private ProductDAO productDAO;

//  @Autowired
//  private BranchDAO branchDAO;

//  @Autowired
//  private EmployeeDAO employeeDAO;

  @Autowired
  private SalesDAO salesDAO;

  @Autowired
  private VehicleDAO vehicleDAO;

//  @Autowired
//  private ADAO aDAO;

//  @Autowired
//  private ReportingDAO reportingDAO;

//  @Autowired
//  private Test2DAO test2DAO;

  @Autowired
  private TestDAO testDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      log.info("[ Starting... ]");
      testComplement();
//      testCast();
//      testWhere();
//      testInsert1();
//      testForEach();
//      testOracleInsertSeq();
//      testSubExpressions();
//      testA();
//      testParseRow();
//      insertConverter();
//      converterOnPK();
//      updateConverter();
//      testQueries();
//      testInsertNonId();
//      selectByExample();
//      testParseRow();
//      testPredicate();
//      testSequence();
//      testInsert();
//      test();
//      testLiveSQL();
//      testLiveSQLUnary();
//      testLiveSQLTuples();
//      tuplesExamples();
      // testLiveSQLCursor();
//      testConverter5();
//      testConverter6();
//      testOptimisticLocking();
//      testNitro6();
      log.info("[ Ending ]");
    };
  }

  private void testComplement() {
    List<Integer> codigos = Arrays.asList(123, 456, 789);
    List<MyAccount> ma = this.salesDAO.findMyAccounts(codigos);
    ma.forEach(r -> System.out.println("ma=" + r));
  }

  private void testCast() {
    ProductTable p = this.productDAO.newTable();
    List<Row> rows = this.sql.select(p.pidProduct, p.pidProduct.castChar("VARCHAR").length().as("len")).from(p)
        .execute();
    rows.forEach(r -> System.out.println("r=" + r));
  }

  private void testWhere() {
    VehicleLayout example = new VehicleLayout();
    example.setName("Toyota 1");
    example.setVehicleCode(1123);

    System.out.println("=== SELECT BY PK ===");
    Vehicle v = this.vehicleDAO.select(1234);

    System.out.println("=== SELECT BY EXAMPLE ===");
    this.vehicleDAO.select(example);

    System.out.println("=== UPDATE BY PK ===");
    this.vehicleDAO.update(v);

    System.out.println("=== UPDATE BY EXAMPLE ===");
    this.vehicleDAO.update(example, example);

    System.out.println("=== DELETE BY PK ===");
    this.vehicleDAO.delete(1234);

    System.out.println("=== DELETE BY EXAMPLE ===");
    this.vehicleDAO.delete(example);

//    System.out.println("inserted=" + inserted);

  }

  private void testInsert1() {
    VehicleLayout v = new VehicleLayout();
    v.setName("Toyota 1");
    v.setVehicleCode(1123);

    Vehicle inserted = this.vehicleDAO.insert(v);
    System.out.println("inserted=" + inserted);

  }

  private void testSelect1() throws SQLException {
//    ProductTable p = this.productDAO.newTable();
//    List<Tuple1<Product>> products = this.sql.select(
//        p.star(), p.shipping.plus(p.tax).as("totalCost")
//        ) //
//        .tuples() //
//        .from(p) //
//        .where(p.type.eq("SPORTS")) //
//        .orderBy(p.shipping.desc()) //
//        .limit(50)
//        .execute();
  }

  private void testForEach() throws SQLException {
    System.out.println("Searching products...");

    Long[] ids = new Long[] { 100L, 102L, 104L };
    List<Product> products = this.testDAO.select(ids);
    products.forEach(p -> System.out.println("p=" + p));

    ids = new Long[] { 105L, 106L };
    products = this.testDAO.select(ids);
    products.forEach(p -> System.out.println("p=" + p));

  }

  private void testOracleInsertSeq() throws SQLException {
//    System.out.println("DB: " + this.productDAO.getDataSource().getConnection().getMetaData().getURL());

    Product p = new Product();
    p.setShipping(120);
    p.setType("ghi");
    Product inserted = this.productDAO.insert(p);
    System.out.println("inserted=" + inserted);
  }

//  private void testSubExpressions() {
//    VehicleTable v = this.vehicleDAO.newTable();
//    List<Row> rows = this.sql //
//        .select(v.name, v.name.as("v2"), v.vehicleCode, v.vehicleCode.as("code").type(Double.class)) //
//        .from(v) //
//        .where(v.name.like(v.name)) //
//        .execute();
//    rows.forEach(r -> System.out.println("r=" + r));
//  }
//
//  private void testParseRow() {
//    AccountTable a = this.accountDAO.newTable();
//    CoinTable c = this.coinDAO.newTable();
//    List<Row> rows = this.sql.select( //
//        a.star().as(ac -> "a:" + ac.getProperty()), //
//        c.star().as(cc -> "c:" + cc.getProperty()) //
//    ).from(a) //
//        .crossJoin(c) //
//        .where( //
////            c.name.like("F%")
//            a.balance.gt(400).and(c.name.like("F%"))) //
//        .execute();
//    for (Row r : rows) {
//      System.out.println("r=" + r);
//      Account pa = this.accountDAO.parseRow(r, "a:");
//      System.out.println("pa=" + pa);
//      Coin pc = this.coinDAO.parseRow(r, "c:");
//      System.out.println("pc=" + pc);
//    }
//  }
//
//  private void insertConverter() {
//    Account a = new Account();
//    a.setName("CHCC1");
//    a.setBalance(1111.1);
//    a.setUpdatedAt(LocalDateTime.now());
//    a.setVersion(1);
//
//    a.setType(AccountType.PEN1);
//    a.setActive(true);
//
//    Account inserted = this.accountDAO.insert(a);
//    System.out.println("inserted: " + inserted);
//
//    inserted.setType(AccountType.INV4);
//    int count = this.accountDAO.update(inserted);
//    System.out.println("updated: " + count);
//
//    Account example = new Account();
//    example.setType(AccountType.INV4);
//    count = this.accountDAO.delete(example);
//    System.out.println("updated: " + count);
//  }
//
//  private void converterOnPK() {
//    Coin c = new Coin();
//    c.setType(2);
//    c.setName("Coin 2");
//    Coin inserted = this.coinDAO.insert(c);
//    System.out.println("inserted: " + inserted);
//
//    CoinTable ct = this.coinDAO.newTable();
//    List<Row> rows = this.sql.select().from(ct).execute();
//    for (Row r : rows) {
//      System.out.println("r=" + r);
//    }
//
//    inserted.setName("Coin 2 b)");
//    int count = this.coinDAO.update(inserted);
//    System.out.println("updated: " + count);
//
//    Coin example = new Coin();
//    example.setName("Coin 2 b)");
//    count = this.coinDAO.delete(example);
//    System.out.println("delete: " + count);
//  }
//
//  private void updateConverter() {
//    Account a = this.accountDAO.select(123);
//    a.setActive(false);
//    int count = this.accountDAO.update(a);
//    System.out.println("updated: " + count);
//  }
//
//  private void testQueries() {
//
////    int rows = this.employeeDAO.updateEmployees();
////    System.out.println("updated employees=" + rows);
////
////    rows = this.salesDAO.deleteAccounts();
////    System.out.println("deleted accounts=" + rows);
//
//  }
//
//  private void testInsertNonId() {
//    VehicleLayout v = new VehicleLayout();
//    v.setName("Buick");
//    Vehicle vm = this.vehicleDAO.insert(v);
//    System.out.println("vm=" + vm);
//
//    v.setName("Toyota");
//    vm = this.vehicleDAO.insert(v);
//    System.out.println("vm=" + vm);
//
//  }
//
//  private void selectByExample() throws DynamicExpressionException, SQLException {
////    AccountLayout example = new AccountLayout();
////    List<Account> accounts = this.accountDAO.select(example);
////    for (Account a : accounts) {
////      System.out.println("a=" + a);
////    }
//
//    EmployeeLayout example = new EmployeeLayout();
//    List<Employee> employees = this.employeeDAO.select(example);
//    employees.forEach(e -> System.out.println("emp=" + e));
//
////    EmployeeLayout example = new EmployeeLayout();
////    List<Employee> employees = this.employeeDAO.findVIPEmployees();
////    employees.forEach(e -> System.out.println("emp=" + e));
//
////    List<BigAccount> bas = this.salesDAO.findBigAccounts();
////    bas.forEach(a -> System.out.println("big account=" + a));
//
////   long seq = this.salesDAO.getHiredNextValue();
////    System.out.println("seq=" + seq);
//
//  }
//
//  private void testA() throws SQLException, DynamicExpressionException {
//    Account a = this.accountDAO.select(123);
//    System.out.println("a=" + a);
//  }

//  private void testParseRow() throws SQLException, DynamicExpressionException {
//    EmployeeTable e = this.employeeDAO.newTable();
//    BranchTable b = this.branchDAO.newTable();
//
//    List<Row> rows = this.sql.select( //
//        e.star().as(c -> "e:" + c.getProperty()), //
//        b.star().as(c -> "b:" + c.getProperty()), //
//        e.name.as("oldNmae") //
//    ) //
//        .from(e).crossJoin(b).limit(1).execute();
//
//    try (Connection conn = this.employeeDAO.getDataSource().getConnection()) {
//      for (Row row : rows) {
//        Employee emp = this.employeeDAO.parseRow(row, "e:", conn);
//        System.out.println("e=" + emp);
//        Branch bra = this.branchDAO.parseRow(row, "b:");
//        System.out.println("b=" + bra);
//      }
//    }
//
//  }
//
//  private void testPredicate() throws SQLException, DynamicExpressionException {
//    EmployeeTable t = this.employeeDAO.newTable();
//
////    Predicate p = t.name.like("%nne%");
////    List<Employee> es = this.employeeDAO.select(t, p).execute();
////    for (Employee e : es) {
////      System.out.println("--> e=" + e);
////    }
//
////    Predicate p2 = t.id.eq(30);
////    Employee e2 = this.employeeDAO.select(t, p2).executeOne();
////    System.out.println("--> e2=" + e2);
//
//    Predicate p = t.name.like("%nne%");
//    Cursor<Employee> es = this.employeeDAO.select(t, p).executeCursor();
//    for (Employee e : es) {
//      System.out.println("--> e=" + e);
//    }
//
//  }
//
//  private void testSequence() throws SQLException, DynamicExpressionException {
//    long seq = this.employeeDAO.getSequenceNextValue();
//    System.out.println("--> seq=" + seq);
//    long hiredSeq = this.employeeDAO.getHiredNextValue();
//    System.out.println("--> hired_seq=" + hiredSeq);
//    seq = this.employeeDAO.getSequenceNextValue();
//    System.out.println("--> seq=" + seq);
//  }
//
//  private void testInsert() throws SQLException, DynamicExpressionException {
//    Branch a = new Branch();
//    a.setRegion("NNW");
//    a.setIsVip(1);
//    a.setParentBranchId(null);
//    a.setCreatedAt(LocalDateTime.now());
//    Branch b = this.branchDAO.insert(a);
//    System.out.println("--> b=" + b);
//
//    // Update by PK
//
//    b.setRegion("NNW01");
//    int count = this.branchDAO.update(b);
//    System.out.println("--> UPDATE count=" + count);
//
//    // Update by example
//
//    Branch example = new Branch();
//    example.setIsVip(1);
//    Branch values = new Branch();
//    values.setIsVip(1);
//    int c2 = this.branchDAO.update(example, values);
//    System.out.println("--> UPDATE2 count=" + c2);
//
//    // Update with predicate
//
//    BranchTable x = this.branchDAO.newTable();
//    int c3 = this.branchDAO.update(values, x, x.id.ge(100)).execute();
//    System.out.println("--> UPDATE3 count=" + c3);
//
//    // LiveSQL Update
//
//    int c4 = this.sql.update(x).set(x.isVip, 1).where(x.id.ge(100)).execute();
//    System.out.println("--> UPDATE4 count=" + c4);
//
//  }

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
////  }
//
//  private void testLiveSQLUnary() throws SQLException, DynamicExpressionException {
//
//    AccountTable a = this.accountDAO.newTable();
//    AccountTable a2 = this.accountDAO.newTable();
//
////    Subquery x = sql.subquery("x", sql.select(sql.val(100).mult(1.1).as("cost"), a.balance).from(a).limit(1));
//
////    Subquery x = sql.subquery("x", sql.select(a.balance.as("balx")).from(a).limit(1));
////    Subquery y = sql.subquery("y", sql.select(x.num("balx").as("baly")).from(x).limit(1));
//
//////    Subquery x = sql.subquery("x", sql.select(a.star().filter(c->c.getProperty().equals("balance") ||c.getProperty().equals("name"))).from(a).limit(1));
////    Subquery x = sql.subquery("x", sql.select(a.star().filter(c->c.getProperty().equals("balance") )).from(a).limit(1));
////    Subquery y = sql.subquery("y", sql.select(x.num("balance").as("baly")).from(x).limit(1));
////    List<Row> rows = sql.select(y.num("baly").as("balm")) //
//////    List<Row> rows = sql.select(x.num("cost"), x.num("balance")) //
////        .from(y) //
////        .execute();
//
////  Subquery x = sql.subquery("x", sql.select(a.star().filter(c->c.getProperty().equals("balance") ||c.getProperty().equals("name"))).from(a).limit(1));
////    Subquery x = sql.subquery("x", sql.select(a.star().filter(c->c.getProperty().equals("balance") )).from(a).limit(1));
//
////    Subquery x = sql.subquery("x", sql.select(a.id, a.name, a.updatedAt).from(a).limit(1));
//    Subquery x = sql.subquery("x", sql.select(a.balance.as("balx"), a.name, a.updatedAt).from(a).limit(1));
//
//    Subquery y = sql.subquery("y",
//        sql.select(x.num("balx").as("baly"), x.str("name"), x.dt("updatedAt")).from(x).limit(1));
//    Select<Row> q = sql.select(y.num("baly").as("balm"), y.str("name").as("namy"), y.dt("updatedAt").as("updated"),
//        sql.literal(123).mult(3).as("total")) //
////  List<Row> rows = sql.select(x.num("cost"), x.num("balance")) //
//        .from(y);
////    System.out.println("=== QUERY ===\n" + q.getPreview(true));
//    List<Row> rows = q.execute();
//
////    List<Row> rows = sql.select(sql.literal(123).mult(3).as("total")).execute();
////    List<Row> rows = sql.select(sql.literal(123).mult(3).as("total")).execute();
//
////    Subquery x = sql.subquery("x", sql.select(sql.val(100).mult(1.1).as("cost"), a2.balance).from(a2).limit(1));
////
////    List<Row> rows = sql.select( //
////
////        sql.val(3).mult(7).as("m1"), //
////        a.id, //
////        x.num("cost"), //
////        x.num("balance"), //
////        //
////        sql.val(3).mult(7).as("multi"), //
////        a.id.as("bid"), //
////        x.num("cost").as("total") //
////
////    ) //
////        .from(a) //
////        .crossJoin(x) //
////        .execute();
//
//    for (Row r : rows) {
//      System.out.println("r=" + r);
//    }
//
//  }
//
//  private void testLiveSQLTuples() throws SQLException, DynamicExpressionException {
//
////    AccountTable a = this.accountDAO.newTable();
////    ProductTable p = this.productDAO.newTable("p");
////    BranchTable b1 = this.branchDAO.newTable();
////    BranchTable b2 = this.branchDAO.newTable();
//
////    Select<Row> q = this.sql.select(a.star(), p.shipping, p.type.as("ptype")).from(a).crossJoin(p).limit(2);
////    System.out.print(q.getPreview(true));
////    q.execute().forEach(r -> System.out.println("r=" + r));
//
////    Subquery x = sql.subquery("x", sql.select(sql.max(b1.createdAt).as("mca")).from(b1));
////    Subquery y = sql.subquery("y", sql.select(sql.max(b2.createdAt).as("mca")).from(b2));
//
////    SelectTuplesFrom2Phase<Account, Product> q = this.sql.select( //
////        a.star(), p.shipping, a.balance.mult(2).as("bal2") //
////        , p.id //
////        , p.type.as("ptype") //
////        , x.dt("mca"), y.dt("mca").as("mca2") // 7
////    ) //
////        .tuples() //
////        .from(x) //
////        .crossJoin(y) //
////        .crossJoin(a) //
////        .crossJoin(p) //
////    ;
//
////    Select<Tuple2<Account, Product>> q = this.sql.select( //
////        a.star(), p.shipping, a.balance.mult(2).as("bal2") //
////        , p.id //
////        , p.type.as("ptype") //
////        , x.dt("mca"), y.dt("mca").as("mca2") // 7
////    ) //
////        .tuples() //
////        .from(x) //
////        .crossJoin(y) //
////        .crossJoin(a) //
////        .crossJoin(p) //
////        .limit(1);
////
////    List<Tuple2<Account, Product>> rows = q.execute();
////    int n = 1;
////    for (Tuple2<Account, Product> r : rows) {
////      System.out.println("Row #" + n++ + ":");
////      System.out.println("** Account: " + r.getA());
////      System.out.println("** Product: " + r.getB());
////      for (String prop : r.getUnbound().keySet()) {
////        System.out.println("** unbound '" + prop + "': " + r.getUnbound().get(prop));
////      }
////    }
//
////    Select<Tuple1<Account>> q = this.sql.select(a.star()) //
////        .tuples() //
////        .from(a) //
////        .where(a.id.eq(112));
////
////    List<Tuple1<Account>> rows = q.execute();
////    int n = 1;
////    for (Tuple1<Account> r : rows) {
////      System.out.println("Row #" + n++ + ":");
////      System.out.println("** Account: " + r.getA());
////      for (String prop : r.getUnbound().keySet()) {
////        System.out.println("** unbound '" + prop + "': " + r.getUnbound().get(prop));
////      }
////    }
//
////    CTE x = sql.cte("x", sql.select(sql.max(b1.createdAt).as("mca")).from(b1));
////    CTE y = sql.cte("y", sql.select(sql.max(b2.createdAt).as("mca")).from(b2));
//
//    AccountTable a1 = this.accountDAO.newTable();
//    AccountTable a2 = this.accountDAO.newTable();
//    AccountTable a3 = this.accountDAO.newTable();
//    AccountTable a4 = this.accountDAO.newTable();
//
////    Select<Tuple5<Account, Account, Account, Account, Account>> q = this.sql.with(x, y).select(a.star(), a1.balance) //
////        .tuples() //
////        .from(a) //
////        .crossJoin(x) //
////        .crossJoin(y) //
////        .crossJoin(a1) //
////        .crossJoin(a2) //
////        .crossJoin(a3) //
////        .crossJoin(a4) //
//////        .where(a.id.gt(1)).limit(1) //
//////        .where(a.id.eq(112)) //
////        .limit(1);
////
////    List<Tuple5<Account, Account, Account, Account, Account>> rows = q.execute();
////    int n = 1;
////    for (Tuple5<Account, Account, Account, Account, Account> r : rows) {
////      System.out.println("Row #" + n++ + ":");
////      System.out.println("** Account A: " + r.getA());
////      System.out.println("** Account A1: " + r.getB());
////      for (String prop : r.getUnbound().keySet()) {
////        System.out.println("** unbound '" + prop + "': " + r.getUnbound().get(prop));
////      }
////    }
//
//    List<Row> rows = this.sql.select().from(a1).limit(1).execute();
//    for (Row r : rows) {
//      System.out.println("r=" + r);
//    }
//
////    List<Tuple1<Account>> rows = this.sql.select().tuples().from(a1).limit(1).execute();
////    for (Tuple1<Account> r : rows) {
////      System.out.println("** Account" + r.getA());
////      for (String prop : r.getUnbound().keySet()) {
////        System.out.println("** unbound '" + prop + "': " + r.getUnbound().get(prop));
////      }
////    }
//
////    List<Tuple2<Account, Account>> rows = this.sql.select(a1.id, a2.id).tuples().from(a1).crossJoin(a2)
////        .where(a1.id.ne(a2.id)).limit(1).execute();
////    for (Tuple2<Account, Account> r : rows) {
////      System.out.println("** Account Left: " + r.getA());
////      System.out.println("** Account Right: " + r.getB());
////      for (String prop : r.getUnbound().keySet()) {
////        System.out.println("** unbound '" + prop + "': " + r.getUnbound().get(prop));
////      }
////    }
//
//  }
//
//  private void tuplesExamples() throws SQLException, DynamicExpressionException {
////    tuplesExample1();
////    tuplesExample2();
////    tuplesExample3();
////    tuplesExample4();
////    tuplesExample5();
////    tuplesExample6();
////    tuplesExample7();
////    tuplesExample7();
////    tuplesExample8();
////    tuplesExample9();
////    tuplesExample10();
//  }
//
//  private void tuplesExample1() throws SQLException, DynamicExpressionException {
//
//    // Equivalent to "Select by Criteria"; uses correct types, converters, etc.
//
//    AccountTable a = this.accountDAO.newTable();
//
//    List<Tuple1<Account>> rows = this.sql.select().tuples() //
//        .from(a) //
//        .where(a.balance.ge(500)) //
//        .execute();
//    for (Tuple1<Account> r : rows) {
//      Account account = r.getA();
//      System.out.println("=== Account: " + account);
//    }
//
////    === Account: app.persistence.model.Account@5eb5da12
////    - id=111
////    - name=1072
////    - type=CHK
////    - balance=500.0
////    - active=false
////    - updatedAt=2025-08-11T11:37:07.597668
////    - version=1
//
//  }
//
//  private void tuplesExample2() throws SQLException, DynamicExpressionException {
//
//    // Filtering columns (excluding tuples columns)
//
//    AccountTable a = this.accountDAO.newTable();
//
//    List<Tuple1<Account>> rows = this.sql.select( //
//        a.id, //
//        a.balance, //
//        a.star().filter(c -> !c.getType().equals("BINARY LARGE OBJECT")) //
////        a.star().filter(c -> {
////          System.out.println(c.getProperty() + ":" + c.getType());
////          return c.getType().equals("TIMESTAMP");
////        }) //
//    ) //
//        .tuples() //
//        .from(a) //
//        .where(a.balance.ge(500)) //
//        .execute();
//    for (Tuple1<Account> r : rows) {
//      Account account = r.getA();
//      System.out.println("=== Account: " + account);
//    }
//
////    === Account: app.persistence.model.Account@307e4c44
////        - id=111
////        - name=1072
////        - type=CHK
////        - balance=500.0
////        - active=false
////        - clientPhoto=null
////        - updatedAt=2025-08-12T10:12:04.273448
////        - version=1
//
//  }
//
//  private void tuplesExample3() throws SQLException, DynamicExpressionException {
//
//    // Tuple and non-tuple columns (unbound columns)
//
//    AccountTable a = this.accountDAO.newTable();
//
//    List<Tuple1<Account>> rows = this.sql.select( //
//        a.id, // tuple
//        a.balance, // tuple
//        a.name.as("accountNumber"), // non-tuple
//        a.balance.plus(150).as("score") // non-tuple
//    ).tuples() //
//        .from(a) //
//        .where(a.balance.ge(460)) //
//        .execute();
//    for (Tuple1<Account> r : rows) {
//      Account account = r.getA();
//      System.out.println("=== Account: " + account);
//      for (String prop : r.getUnbound().keySet()) {
//        System.out.println("*** Unbound '" + prop + "': " + r.getUnbound().get(prop));
//      }
//    }
//
////    === Account: app.persistence.model.Account@1946384
////        - id=111
////        - name=null
////        - type=null
////        - balance=500.0
////        - active=null
////        - clientPhoto=null
////        - updatedAt=null
////        - version=null
////        *** Unbound 'score': 650
////        *** Unbound 'accountNumber': 1072
//
//  }
//
////  private void tuplesExample4() throws SQLException, DynamicExpressionException {
////
////    // Joining tables and views
////
////    BranchTable b = this.branchDAO.newTable();
////    EmployeeTable e = this.employeeDAO.newTable();
////
//////    List<Tuple2<Employee, Branch>> rows = this.sql.select().tuples() //
//////        .from(e) //
//////        .join(b, b.id.eq(e.branchId)).where(e.name.like("%Anne%")) //
//////        .execute();
//////    for (Tuple2<Employee, Branch> r : rows) {
//////      Employee account = r.getA();
//////      Branch branch = r.getB();
//////      System.out.println("=== Employee: " + account);
//////      System.out.println("=== Branch: " + branch);
//////    }
////
////    List<Tuple1<Employee>> rows2 = this.sql.select().tuples() //
////        .from(e) //
////        .semiJoin(b, b.id.eq(e.branchId)).where(e.name.like("%Anne%")) //
////        .execute();
////    for (Tuple1<Employee> r : rows2) {
////      Employee account = r.getA();
////      System.out.println("=== Employee: " + account);
////    }
////
//////    === Employee: app.persistence.model.Employee@6adc5b9c
//////        - id=30
//////        - name=Anne
//////        - branchId=101
//////    === Branch: app.persistence.model.Branch@19c1820d
//////        - id=101
//////        - region=N
//////        - isVip=1
//////        - parentBranchId=null
//////        - createdAt=2024-01-01T12:34:56
////
////  }
////
////  private void tuplesExample5() throws SQLException, DynamicExpressionException {
////
////    // Self joins
////
////    BranchTable b = this.branchDAO.newTable();
////    BranchTable p = this.branchDAO.newTable();
////
////    List<Tuple2<Branch, Branch>> rows = this.sql.select().tuples() //
////        .from(b) //
////        .join(p, p.id.eq(b.parentBranchId)) //
////        .where(b.region.eq("NE")).execute();
////    for (Tuple2<Branch, Branch> r : rows) {
////      Branch branch = r.getA();
////      Branch parentBranch = r.getB();
////      System.out.println("=== Branch: " + branch);
////      System.out.println("=== Parent Branch: " + parentBranch);
////    }
////
//////    === Branch: app.persistence.model.Branch@4b862408
//////        - id=105
//////        - region=NE
//////        - isVip=0
//////        - parentBranchId=101
//////        - createdAt=2024-01-05T12:34:56
//////    === Parent Branch: app.persistence.model.Branch@6ddee60f
//////        - id=101
//////        - region=N
//////        - isVip=1
//////        - parentBranchId=null
//////        - createdAt=2024-01-01T12:34:56
////
////  }
////
////  private void tuplesExample6() throws SQLException, DynamicExpressionException {
////
////    // Joining multiple tables and views (up to 26)
////
////    EmployeeTable e = this.employeeDAO.newTable();
////    BranchTable b = this.branchDAO.newTable();
////    BranchTable p = this.branchDAO.newTable();
////
////    List<Tuple3<Employee, Branch, Branch>> rows = this.sql //
////        .select() //
////        .tuples() //
////        .from(e) //
////        .join(b, e.branchId.eq(b.id)) //
////        .join(p, p.id.eq(b.parentBranchId)) //
////        .execute();
////    for (Tuple3<Employee, Branch, Branch> r : rows) {
////      Employee employee = r.getA();
////      Branch branch = r.getB();
////      Branch parentBranch = r.getC();
////      System.out.println("=== Employee: " + employee);
////      System.out.println("=== Branch: " + branch);
////      System.out.println("=== Parent Branch: " + parentBranch);
////    }
////
//////    === Employee: app.persistence.model.Employee@6418e39e
//////        - id=33
//////        - name=Malcolm
//////        - branchId=107
//////    === Branch: app.persistence.model.Branch@3635099
//////        - id=107
//////        - region=SE
//////        - isVip=0
//////        - parentBranchId=102
//////        - createdAt=2024-01-07T12:34:56
//////    === Parent Branch: app.persistence.model.Branch@1da1380b
//////        - id=102
//////        - region=S
//////        - isVip=1
//////        - parentBranchId=null
//////        - createdAt=2024-01-02T12:34:56
////
////  }
////
//////  private void tuplesExampleNested() throws SQLException, DynamicExpressionException {
//////
//////    // Joining multiple tables and views (up to 26)
//////
//////    EmployeeTable e = this.employeeDAO.newTable();
//////    BranchTable b = this.branchDAO.newTable();
//////    BranchTable p = this.branchDAO.newTable();
//////
//////    List<Tuple1<Employee>> rows = this.sql //
//////        .select( //
//////            e.star(), //
//////            
//////            sql.collection("parentBranches").columns(b.createdAt, p.isVip), //
//////            sql.collection("parentBranches").over(e.name).columns(b.createdAt, p.isVip), //
//////
//////            b.association("currentBranch").columns(b.id, b.region), //
//////            b.collection("parentBranches").over(e.name), //
//////            b.collection("parentBranches").over(e.name).columns(b.id, b.region) //
//////
//////            b.collection("parentBranches").over(e.name).columns(b.id, b.region,
//////                p.association("parents"),
//////                sql.collection("items").over(b.id).columns(e.name.concat(p.region).as("code"))
//////                ) //
//////
//////            ) //
//////        .collections() //
//////        .from(e) //
//////        .semiJoin(b, e.branchId.eq(b.id)) //
//////        .semiJoin(p, p.id.eq(b.parentBranchId)) //
//////        .execute();
//////    for (Tuple1<Employee> r : rows) {
//////      Employee employee = r.getA();
////////      Branch branch = r.getB();
////////      Branch parentBranch = r.getC();
//////      System.out.println("=== Employee: " + employee);
////////      System.out.println("=== Branch: " + branch);
////////      System.out.println("=== Parent Branch: " + parentBranch);
//////    }
//////
//////  }
////
////  private void tuplesExample7() throws SQLException, DynamicExpressionException {
////
////    EmployeeTable e = this.employeeDAO.newTable();
////    BranchTable b = this.branchDAO.newTable();
////    BranchTable p = this.branchDAO.newTable();
////
////    List<Tuple2<Employee, Branch>> rows = this.sql.select().tuples().from(e).join(b, e.branchId.eq(b.id))
////        .semiLeftJoin(p, p.parentBranchId.eq(b.id)).where(p.id.isNull()).execute();
////    for (Tuple2<Employee, Branch> r : rows) {
////      Employee employee = r.getA();
////      Branch branch = r.getB();
////      System.out.println("=== Employee: " + employee);
////      System.out.println("=== Branch: " + branch);
////    }
////
//////    === Employee: app.persistence.model.Employee@2ec99035
//////        - id=32
//////        - name=Jeanne
//////        - branchId=104
//////        === Branch: app.persistence.model.Branch@60743cdb
//////        - id=104
//////        - region=E
//////        - isVip=0
//////        - parentBranchId=null
//////        - createdAt=2024-01-04T12:34:56
////
////  }
////
////  private void tuplesExample8() throws SQLException, DynamicExpressionException {
////
////    // Joining subqueries and CTEs
////
////    BranchTable br = this.branchDAO.newTable();
////    CTE y = sql.cte("y", sql.select(sql.min(br.region).as("minRegion")).from(br));
////
////    EmployeeTable e = this.employeeDAO.newTable();
////    Subquery x = sql.subquery("x", sql.select(sql.currentDate().as("currentDate")));
////    BranchTable b = this.branchDAO.newTable();
////
////    List<Tuple2<Employee, Branch>> rows = this.sql.with(y).select().tuples() //
////        .from(e) //
////        .crossJoin(x) //
////        .crossJoin(y) //
////        .join(b, b.id.eq(e.branchId).and(b.region.eq(y.str("minRegion")))) //
////        .execute();
////
////    for (Tuple2<Employee, Branch> r : rows) {
////      Employee employee = r.getA();
////      Branch branch = r.getB();
////      System.out.println("=== Employee: " + employee);
////      System.out.println("=== Branch: " + branch);
////      for (String prop : r.getUnbound().keySet()) {
////        System.out.println("*** Unbound '" + prop + "': " + r.getUnbound().get(prop));
////      }
////    }
////
//////    === Employee: app.persistence.model.Employee@21de60a7
//////        - id=32
//////        - name=Jeanne
//////        - branchId=104
//////        === Branch: app.persistence.model.Branch@73894c5a
//////        - id=104
//////        - region=E
//////        - isVip=0
//////        - parentBranchId=null
//////        - createdAt=2024-01-04T12:34:56
//////        *** Unbound 'currentDate': 2025-08-12
//////        *** Unbound 'minRegion': E
////
////  }
//
//  private void tuplesExample9() throws SQLException, DynamicExpressionException {
//
//    // Select Using Cursors
//
//    AccountTable a = this.accountDAO.newTable();
//
//    Cursor<Tuple1<Account>> rows = this.sql.select().tuples() //
//        .from(a) //
//        .where(a.balance.ge(500)) //
//        .executeCursor();
//    for (Tuple1<Account> r : rows) {
//      Account account = r.getA();
//      System.out.println("=== Account: " + account);
//    }
//
////    === Account: app.persistence.model.Account@25a94b55
////        - id=111
////        - name=1072
////        - type=CHK
////        - balance=500.0
////        - active=false
////        - updatedAt=2025-08-11T13:40:27.216758
////        - version=1
//
//  }
//
//  private void tuplesExample10() throws SQLException, DynamicExpressionException {
//
//    // Select a Single Row
//
//    AccountTable a = this.accountDAO.newTable();
//
//    Tuple1<Account> row = this.sql.select().tuples() //
//        .from(a) //
//        .where(a.balance.ge(450)) //
//        .orderBy(a.updatedAt.desc()).limit(1).executeOne();
//    Account account = row.getA();
//    System.out.println("=== Account: " + account);
//
////    === Account: app.persistence.model.Account@526e8108
////        - id=111
////        - name=1072
////        - type=CHK
////        - balance=500.0
////        - active=false
////        - clientPhoto=[B@4dcbae55
////        - updatedAt=2025-08-12T11:57:51.662793
////        - version=1
//
//  }
//
//  private void testLiveSQL() throws SQLException, DynamicExpressionException {
//
////    Account ax = this.accountDAO.select(123);
////    System.out.println(">> Account 123: " + ax);
//
////    AccountTable a = this.accountDAO.newTable();
////    List<Account> accounts = this.accountDAO.select(a, a.balance.gt(500)).execute();
////    System.out.println(">> Accounts:");
////    accounts.forEach(r -> System.out.println(r));
//
////    Select<Row> s = this.sql.select(sql.val(7).mult(3).as("answer"));
////    System.out.println("Query:" + s.getPreview());
////    Row row = s.executeOne();
////    System.out.println("Row=" + row);
//
////    System.out.println("Will UPDATE by criteria.");
////    AccountTable a = this.accountDAO.newTable();
////    Account updateValues = new Account();
////    updateValues.setBalance(777);
////    int count = this.accountDAO.update(updateValues, a, a.balance.lt(150)).execute();
////    System.out.println("UPDATE by criteria complete: count=" + count);
////
////    List<Account> accounts = this.accountDAO.select(a, sql.TRUE).execute();
////    accounts.forEach(r -> System.out.println("r=" + r));
//
////    System.out.println("Will select by criteria." );
////    AccountTable a = this.accountDAO.newTable();
////    List<Account> accounts = this.accountDAO.select(a, a.type.eq("CHK")).orderBy(a.balance.desc()).execute();
////    for (Account r : accounts) {
////      System.out.println("account=" + r);
////    }
////    System.out.println("Select by criteria complete." );
//
////    System.out.println("Will DELETE by criteria.");
////    AccountTable a = this.accountDAO.newTable();
////    int count = this.accountDAO.delete(a, a.type.eq("SAV")).execute();
////    System.out.println("DELETE by criteria complete: count=" + count);
//
////    List<Row> rows = this.sql.select().from(a).execute();
////    for (Row r : rows) {
////      System.out.println("Row=" + r);
////    }
//  }
//
//  private void test() throws SQLException, DynamicExpressionException {
//
////    Account a = this.accountDAO.select(112);
////    System.out.println("--> a=" + a);
//
////    List<Integer> ids = Arrays.asList(123, 789, 112, 4);
//
////    int rows = this.reportingDAO.activateBigAccounts(125L, ids);
////      int rows=  this.reportingDAO.activateBigAccounts2(125L, ids, "type = 'CHK'");
//
//    // List<BigAccount> bas = this.reportingDAO.findBigAccounts();
////    for (BigAccount ba : bas) {
////      System.out.println("--> " + ba);
////    }
//
////    List<Account> bas = this.accountDAO.findBigAccounts();
////    for (Account ba : bas) {
////      System.out.println("--> " + ba);
////    }
//
//    // int rows = this.accountDAO.activateBigAccounts(null);
////    System.out.println("--> rows=" + rows);
//
////    Account filter = new Account();
////    filter.setType("CHK");
////
////    List<Account> accounts = this.accountDAO.select(filter, AccountOrderBy.ID, AccountOrderBy.NAME$DESC);
////    for (Account a : accounts) {
////      System.out.println("--> " + a);
////    }
//
////  List<Account> accounts = this.accountDAO.select1();
////  for (Account a : accounts) {
////    System.out.println("--> " + a);
////  }
//
////    Account a = new Account();
////    a.setId(400);
////    a.setName("ACC123");
////    a.setType(null);
////    a.setBalance(true);
////
////    this.accountDAO.insert(a);
////    System.out.println("> Account inserted -- id=" + a.getId());
////
////    Account filter = new Account();
////    filter.setType("PEN");
////    int cnt = this.accountDAO.delete(filter);
////    System.out.println("> Accounts deleted: " + cnt);
//
//  }

}
