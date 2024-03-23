package app;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.DMLQuery;
import org.hotrod.runtime.livesql.queries.ctes.RecursiveCTE;
import org.hotrod.runtime.livesql.queries.select.CriteriaForUpdatePhase;
import org.hotrod.runtime.livesql.queries.select.EntitySelect;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.plan.PlanRetrieverFactory.UnsupportedTorcsDatabaseException;
import org.hotrod.torcs.rankings.RankingEntry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.BranchVO;
import app.daos.primitives.AccountDAO;
import app.daos.primitives.AccountDAO.AccountTable;
import app.daos.primitives.BranchDAO;
import app.daos.primitives.BranchDAO.BranchTable;
import app.daos.reporting.InvoiceVO;
import app.daos.reporting.primitives.InvoiceDAO;
import app.daos.reporting.primitives.InvoiceDAO.InvoiceTable;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@MapperScan("mappers")
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
@ComponentScan(basePackageClasses = Torcs.class)
//@PropertySource(value = { "file:application.properties",
//    "classpath:application.properties" }, ignoreResourceNotFound = true)
public class App {

//  @Autowired
//  private NumbersDAO numbersDAO;

  @Autowired
  private BranchDAO branchDAO;

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private InvoiceDAO invoiceDAO;

//  @Autowired
//  private DatesDAO datesDAO;

//  @Autowired
//  private CaseDAO caseDAO;

  @Autowired
  private LiveSQL sql;

  @Autowired
  private BusinessLogic businessLogic;

  @Autowired
  private Torcs torcs;

//  @Autowired
//  private TorcsCTP torcsCTP;

//  @Autowired
//  private TorcsCTP torcsCTP;
//
//  @Autowired
//  private PlanRetrieverFactory factory;

//  @Bean
//  public DataSource dataSource() throws SQLException {
//
//    System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
//
//    OracleDataSource ds = new OracleDataSource();
//
//    ds.setURL("jdbc:oracle:thin:@192.168.56.95:1521:orcl");
//    ds.setUser("user1");
//    ds.setPassword("pass1");
//
////    ds.setFastConnectionFailoverEnabled(true);
////    ds.setImplicitCachingEnabled(true);
////    ds.setConnectionCachingEnabled(true);
//
//    Properties props = new Properties();
//    props.setProperty("oracle.jdbc.J2EE13Compliant", "true");
//
//    ds.setConnectionProperties(props);
//    return ds;
//
//  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      
//      DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) applicationContext.getAutowireCapableBeanFactory();
//      registry.destroySingleton("ConfigManager");
//      registry.registerSingleton("ConfigManager", new ConfigManager(filePath));

//      System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");

//      crud();
//      join();
//      join();     
//      livesql();
//      selectByCriteria();
      torcs();
//      star();
//      noFrom();
      System.out.println("[ Example complete ]");
    };
  }

  private void livesql() throws SQLException {
//    for (Row r : this.sql.select(sql.val("abc").ascii().as("code")).execute()) {
//      System.out.println("r=" + r);
//    }
//    for (Row r : this.sql.select(sql.val("abc").ascii().as("code")).execute()) {
//      System.out.println("r=" + r);
//    }

//    NumbersTable n = NumbersDAO.newTable("n");
//    List<Row> rows = sql.select(n.id) //
//        .from(n) //
//        .where(n.id.eq(1).and(sql.enclose(n.int1.lt(4).or(n.dec1.ne(2))).and(n.dec2.ge(3))))//
//        .execute();
//    for (Row r : rows) {
//      System.out.println("r=" + r);
//    }

    // Subqueries
//    joinedTableExpressions();
//    nestedTableExpressions();
//    ctesWithoutNamedColumns();
//    ctesWithNamedColumns();
//    lateralJoins();
//    recursiveCTEs();

    liveSQLExamples();

//    update();

  }

  private void crud() {
//    crudInsert();
    crudSelect();
//    selectCases();
//    selectFK();
  }

  private void crudSelect() {
    List<InvoiceVO> cases = this.invoiceDAO.select(new InvoiceVO());
    cases.forEach(x -> System.out.println("inv:" + x));
  }

//  private void selectCases() {
//    CaseTable c = CaseDAO.newTable("c");
//    EntitySelect<CaseVO> select = this.caseDAO.select(c, c.name.ne("bad")).limit(1);
//    String p = select.getPreview();
//    System.out.println("preview=" + p);
//    List<CaseVO> cases = select.execute();
//    cases.forEach(x -> System.out.println("case:" + x));
//  }

  private void selectFK() {
//    AccountVO a = this.accountDAO.select(1);
//    System.out.println("account=" + a);
//
//    BranchVO b = this.accountDAO.selectParentBranchOf(a).fromBranchId().toId();
//    System.out.println("branch=" + b);
//
//    List<AccountVO> c = this.accountDAO.selectChildrenAccountOf(a).fromId().toParentId();
//    c.forEach(x -> System.out.println("children:" + x));

//    BranchVO b = this.branchDAO.select(101);
//    System.out.println("branch=" + b);
//
//    List<InvoiceVO> is = this.branchDAO.selectChildrenInvoiceOf(b).fromId().toBranchId();
//    is.forEach(x -> System.out.println("children:" + x));
//
//    System.out.println("------------------------");
//
//    InvoiceVO i = this.invoiceDAO.select(12);
//    System.out.println("invoice=" + i);
//
//    BranchVO p = this.invoiceDAO.selectParentBranchOf(i).fromBranchId().toId();
//    System.out.println("parent=" + p);

  }

//  private void crudInsert() {
//    System.out.println("Will INSERT");
//
//    AccountVO a = new AccountVO();
//    a.setBranchId(100);
//    a.setParentId(120);
//    a = this.accountDAO.insert(a);
//
//    System.out.println("INSERTED: a=" + a);
//
//    a = this.accountDAO.insert(a);
//    System.out.println("INSERTED: a=" + a);
//
//    a = this.accountDAO.insert(a);
//    System.out.println("INSERTED: a=" + a);
//  }

//
//  private void joinedTableExpressions() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    // Two separate table expressions
//    Subquery x = sql.subquery("x", sql.select(i.star(), i.id.plus(1).as("subtotal")).from(i));
//    Subquery y = sql.subquery("y", sql.select(b.star(), b.name.concat("//").as("title")).from(b));
//
//    ExecutableSelect<Row> q = sql.select(x.num("id"), x.num("subtotal").mult(2).as("total"), y.str("title")).from(x)
//        .leftJoin(y, y.num("id").eq(x.num("branchId")));
//    System.out.println("1. Table Expressions: " + q.getPreview());
//    q.execute().forEach(r -> System.out.println("r=" + r));
//  }
//
//  private void nestedTableExpressions() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    Subquery x = sql.subquery("x", sql.select(i.star(), i.id.plus(1).as("subtotal")).from(i));
//    Subquery y = sql.subquery("y", sql.select(x.star(), x.num("id").plus(x.num("amount")).as("subtotal2")).from(x));
////  ExecutableSelect<Row> q1 = sql.select().from(x);
////    ExecutableSelect<Row> q1 = sql.select(x.num("id"), x.num("subtotal").mult(2).as("total")).from(x);
//    ExecutableSelect<Row> q1 = sql.select(y.star(), sql.val(123).as("sample")).from(y);
//    System.out.println("1.b: " + q1.getPreview());
//    q1.execute().forEach(r -> System.out.println("r=" + r));
//  }
//
//  private void ctesWithoutNamedColumns() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    // 2. CTEs without named columns
//
//    CTE a = sql.cte("x", sql.select(i.star(), i.id.plus(1).as("subtotal")).from(i));
//    CTE f = sql.cte("y", sql.select(b.star(), b.name.concat("//").as("title")).from(b));
//    ExecutableSelect<Row> q2 = sql.with(a, f).select().from(a).crossJoin(f);
//    System.out.println("2. CTEs without named columns: " + q2.getPreview());
//    q2.execute().forEach(r -> System.out.println("r=" + r));
//
//  }
//
//  private void ctesWithNamedColumns() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    CTE h = sql.cte("h").columnNames("abc", "def", "ghi", "jkl")
//        .as(sql.select(i.star(), i.id.plus(1).as("subtotal")).from(i));
//    CTE k = sql.cte("k").as(sql.select(b.star(), b.name.concat("//").as("title")).from(b));
//    ExecutableSelect<Row> q3 = sql.with(h, k).select().from(h).crossJoin(k);
//    System.out.println("3. CTEs with names columns: " + q3.getPreview());
//    q3.execute().forEach(r -> System.out.println("r=" + r));
//
//  }
//
//  private void lateralJoins() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    Subquery l = sql.subquery("l", //
//        sql.select(i.star()) //
//            .from(i) //
//            .where(i.branchId.eq(b.id)) //
//            .orderBy(i.amount.desc()) //
//            .limit(1) //
//    );
//
//    Subquery l2 = sql.subquery("l2", //
//        sql.select(i.star()) //
//            .from(i) //
//            .where(i.branchId.eq(b.id)) //
//            .orderBy(i.amount.desc()) //
//            .limit(1) //
//    );
//
//    ExecutableSelect<Row> q = sql.select().from(b).joinLateral(l).leftJoinLateral(l2);
//
//    System.out.println("lateral join: " + q.getPreview());
//    q.execute().forEach(r -> System.out.println("r=" + r));
//
//  }

//  private void update() {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//    BranchTable c = BranchDAO.newTable("c");
//
////    // update branch set region = 'x' where id >= 4 and not is_vip
////
////    DMLQuery q = sql.update(b).set(b.region, "x").where(b.id.ge(sql.literal(4)).and(sql.not(b.isVip)));
////    System.out.println("1. Update: " + q.getPreview());
////    int rows = q.execute();
////    System.out.println("updated rows=" + rows);
//
////    // delete from branch where id >= 4 and not is_vip
////
////    DMLQuery q = sql.delete(b).where(b.id.ge(sql.literal(4)).and(sql.not(b.isVip)));
////    System.out.println("1. Update: " + q.getPreview());
////    int rows = q.execute();
////    System.out.println("updated rows=" + rows);
//
//    // insert into branch select id+ 10, region, is_vip from branch where id >= 4
//    // and not is_vip
//
//    DMLQuery q = sql.insert(b).select(sql.select(c.id.plus(sql.literal(10)), c.region, c.isVip).from(c)
//        .where(c.id.ge(sql.literal(4)).and(sql.not(c.isVip.eq("VIP")))));
//    System.out.println("1. Update: " + q.getPreview());
//    int rows = q.execute();
//    System.out.println("updated rows=" + rows);
//
//  }

  private void livesql2() {
    InvoiceTable i = InvoiceDAO.newTable("i");
    Select<Row> q = this.sql.select() //
        .from(i) //
        .where(i.amount.le(sql.literal(2000)).or(i.branchId.eq(sql.literal(102))));
    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));
  }

  private void livesql3() {
    InvoiceTable i = InvoiceDAO.newTable("i");

    CriteriaForUpdatePhase<InvoiceVO> q = this.invoiceDAO //
        .select(i, i.amount.ge(500)) //
        .orderBy(i.branchId.desc()) //
        .offset(1) //
        .limit(5) //
        .forUpdate();
    System.out.println(q.getPreview());

    q.execute().forEach(r -> System.out.println("row: " + r));
  }

  private void livesql4() {
    InvoiceTable i = InvoiceDAO.newTable("i");

    DMLQuery q = this.sql.update(i) //
//        .set(i.amount, 1) //
//        .set(i.amount, (Integer) null) //
        .set(i.amount, sql.NULL) //
        .where(i.id.eq(12));
    System.out.println(q.getPreview());

    int c = q.execute();
    System.out.println("count: " + c);
  }

  private void livesql1() {
    AccountTable a = AccountDAO.newTable("a");

//    List<AccountVO> accounts = this.accountDAO //
//        .select(a, a.branchId.ge(102)) //
//        .orderBy(a.branchId) //
//        .offset(1) //
//        .limit(2) //
//        .execute();
//    accounts.stream().forEach(r -> System.out.println("row: " + r));

//    List<Row> rows = this.sql.select(a.branchId, a.branchId.nullIf(102).as("x")).from(a).execute();
//
//    System.out.println("rows:");
//    rows.stream().forEach(r -> System.out.println("row: " + r));

//    List<Employee> employees = this.employeeDAO
//        .select(e, e.type.ne("MANAGER").and(sql.exists(
//            sql.select().from(m).where(m.branchId.ne(e.branchId).and(m.name.eq(e.name)))
//          ))
//        )
//        .orderBy(e.branchId, e.salary.plus(e.bonus).desc())
//        .execute();    

  }

  private void println(String prompt, Object v) {
    System.out.println("- " + prompt + ": " + v + " (" + (v == null ? "null" : v.getClass().getName()) + ")");
  }

//  private void converter() {
//    AccountTable a = AccountDAO.newTable("a");
//    BranchTable b = BranchDAO.newTable("b");
//
//    Row r = this.sql.select(a.star().as(c -> "a:" + c.getProperty()), b.star().as(c -> "b:" + c.getProperty())).from(b)
//        .join(a, a.branchId.eq(b.id)).where(b.region.like("N%")).orderBy(b.id.desc()).limit(1).executeOne();
//
//    AccountVO account = this.accountDAO.parseRow(r, "a:");
//    BranchVO branch = this.branchDAO.parseRow(r, "b:");
//
//    System.out.println("account=" + account + "\nbranch=" + branch);
//  }

  private void liveSQLExamples() throws SQLException {

//    livesql1();
//    livesql2();
//    livesql3();
//    livesql4();
//    dates();
//    types();
//    converter();
//    forUpdate();
    distinctOn();

    // Set Operators

//    union();

    // Subqueries
//    example1InNotIn();
//    example2ExistsNotExists();
//    example3AssymmetricOperators();
//    example4ScalarSubqueries();
//    example5TableExpressions();
//    example5NestedTableExpressions();
//    example5JoinedTableExpressions();
//    example5NamedTableExpressions();
//    example6CTEs();
//    example7RecursiveCTEs();
//    example8LateralJoins();

  }

//  @Autowired
//  private NumbersDAO tn;
//
//  @Autowired
//  private CharsDAO tc;
//
//  @Autowired
//  private DatesDAO td;
//
//  @Autowired
//  private BinariesDAO tb;
//
//  @Autowired
//  private OtherDAO to;

//  private void types() throws SQLException {
////    System.out.println("\nTYPES_NUMERIC");
////    print(this.sql.select().from(TypesNumericDAO.newTable()).execute());
////
////    System.out.println("\nTYPES_CHAR");
////    print(this.sql.select().from(TypesCharDAO.newTable()).execute());
//
//    System.out.println("\nTYPES_DATE_TIME");
////    print(
//    List<?> li = this.sql.select().from(DatesDAO.newTable()).execute();
//    for (Object obj : li) {
//      System.out.println("obj: " + obj.getClass().getName());
//      DatesVO vo = (DatesVO) obj;
//      System.out.println("vo: " + vo);
//    }
//
//  }

  private void distinctOn() throws SQLException {

    System.out.println("DISTINCT ON");
    InvoiceTable i = InvoiceDAO.newTable("i");

    Select<Row> q = this.sql.selectDistinctOn(i.branchId.plus(sql.literal(100))) //
        .columns(i.branchId.as("myBranch"), i.star()).from(i) //
        .orderBy(i.branchId.plus(sql.literal(100)), i.unpaidBalance.desc());

//    select distinct on (branch_id) *
//    from invoice
//    order by branch_id, unpaid_balance desc

    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void print(List<Row> rows) {
    for (Row r : rows) {
      Set<String> ko = new TreeSet<>(r.keySet());
      for (String k : ko) {
        Object v = r.get(k);
        System.out.println(k + "=" + v + " (" + (v == null ? "null" : v.getClass().getName()) + ")");
      }
    }
  }

//  private void dates() throws SQLException {
//    DatesTable t = DatesDAO.newTable("t");
//
////    System.out.println("ENV PROPERTIES");
////    Map<String, String> env = System.getenv();
////    for (String name : env.keySet()) {
////      String value = env.get(name);
////      System.out.println(" - " + name + "=" + value);
////    }
//
////    System.out.println("ARGUMENTS");
////    RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
////    List<String> arguments = runtimeMxBean.getInputArguments();
////    for (String a : arguments) {
////      System.out.println(" - " + a);
////    }
////    System.out.println("------------------------------------");
//
////    org.apache.ibatis.session.Configuration conf = this.sqlSession.getConfiguration();
////    conf.getEnvironment()
//
//    List<DatesVO> types = this.datesDAO.select(new DatesVO());
//
//    for (DatesVO tp : types) {
//      System.out.println("t:" + tp);
//    }
//
////    Map<String, Object> parameters = new HashMap<>();
////    parameters.
////    List<Object> list = this.typesDateTimeDAO.sqlSession.selectList("", parameters);
//
////    List<Row> rows = this.sql.select().from(t).execute();
////
////    System.out.println("rows:");
////
////    for (Row r : rows) {
////      System.out.println("Row:");
////      println("dat1 (DATE)", r.get("dat1"));
////      println("dat2 (TIMESTAMP)", r.get("dat2"));
////      println("dat3 (TIMESTAMP WITH TIME ZONE)", r.get("dat3"));
////      println("dat4 (TIMESTAMP WITH LOCAL TIME ZONE)", r.get("dat3"));
////    }
//  }

  private void forUpdate() {
    this.businessLogic.forUpdate();
  }

  private void union() {

    AccountTable a = AccountDAO.newTable("a");

//    // 1. UNION + UNION

//    Select<Row> q = sql.select(sql.literal(123).as("abc")) //
////        .union().select(sql.literal(456)) //
//        .union().select(sql.literal(789)) //
//        .orderBy(sql.ordering("abc").desc(), sql.ordering(1)) //
//    ;
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

//    // 2. UNION + EXCEPT

//    Select<Row> q = sql.select(sql.literal(123)) //
//        .union().select(sql.literal(456)) //
//        .except().select(sql.literal(789));
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

    // 3. UNION + INTERSECT

//    Select<Row> q = sql.select(sql.literal(123)) //
//        .unionAll().select(sql.literal(456)) //
//        .intersect().select(sql.literal(789));
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

    // 4. INTERSECT + UNION

//    Select<Row> q = sql.select(sql.literal(123)) //
//        .intersect().select(sql.literal(456)) //
//        .union().select(sql.literal(789));
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

    // 5. UNION + INTERSECT + EXCEPT

//    Select<Row> q = sql.select(sql.literal(123)) //
//        .unionAll().select(sql.literal(456)) //
//        .intersect().select(sql.literal(789)) //
//        .except().select(sql.literal(100))
//        ;
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

    // 6. INTERSECT + EXCEPT + INTERSECT + INTERSECT + UNION

//    Select<Row> q = sql.select(sql.literal(123).as("def")) //
//        .intersect().select(sql.literal(456)) //
//        .except().select(sql.literal(789)) //
//        .intersect().select(sql.literal(100)) //
//        .intersect().select(sql.literal(101)) //
//        .union() //
//        .select(sql.literal(100)) //
//        .orderBy(sql.ordering(1).desc(), sql.ordering("def"))
//    //
//    ;
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

    // 7. INTERSECT + ENCLOSE (UNION) + INTERSECT

//    Select<Row> q = //
//        sql.enclose( //
////        sql.select(sql.literal(1))
////        sql.enclose( //
//            sql.select(sql.literal(123).as("abc")).union().select(sql.literal(456)) //
////        ) //
//        ) //
//            .intersect().select(sql.literal(789)) //
//            .orderBy(sql.ordering("abc"));
////        sql.select(sql.literal(99)).from(a).orderBy(a.id);
//
//    System.out.println(q.getPreview());
//    System.out.println("tree: " + q.getCombinedSelect().toString());
//    q.execute().forEach(r -> System.out.println("row: " + r));

    // 8. NESTED-UNION

    LocalDate ld = LocalDate.now();

//    Select<Row> q = sql.select(sql.literal(123).as("def")) //
////        .union() //
////        .select(sql.literal(100)) //
//        .union(sql.select(sql.literal(200)).except().select(sql.literal(300)).union( //
//            sql.select(sql.literal(Math.PI, 6)) //
//                .from(a) //
//                .orderBy(a.id).limit(100) //
//        )) //
//        .orderBy(sql.ordering(1))
//    //
//    ;
//
//    System.out.println("tree: " + q.getCombinedSelect().toString());
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

    // 9. DateTime Literals

    LocalDate today = LocalDate.now();
    LocalTime currentTime = LocalTime.now();
    LocalDateTime now = LocalDateTime.now();

    OffsetTime x = OffsetTime.of(17, 9, 31, 72000000, ZoneOffset.ofHours(-4));
    OffsetDateTime y = OffsetDateTime.of(2023, 12, 25, 17, 9, 31, 72000000, ZoneOffset.ofHours(-4));

    OffsetTime ocurrentTime = OffsetTime.now();
    OffsetDateTime onow = OffsetDateTime.now();

    Select<Row> q = sql.select( //
        sql.ZERO.as("zero"), //
        sql.ONE.as("one"), //
        sql.FALSE.as("false"), //
        sql.TRUE, //
        sql.NULL.isNull().as("null is null"), //
        sql.NULL.isNotNull().as("null is NOT null"), //
        sql.literal(today).as("today"), //
        sql.literal(currentTime, 3).as("current time"), //
        sql.literal(now, 6).as("now"), //
        sql.literal(onow, 6).as("onow"), //
        sql.literal(ocurrentTime, 3).as("ocurrent time") //
    );

    q.execute().forEach(r -> {
      Object o = r.get("now");
      System.out.println("row: " + r + " now:" + o.getClass().getName());
    });

  }

  // TODO: Nothing to do, just a marker.

  private void torcs() throws SQLException, IOException {
//    disableTorcs();
//  enableTorcs();
//  deactivateDefaultObserverTorcs();
//  changeDefaultObserverSize();
//  changeTorcsResetPeriodTo24Hours();
//  addingAnInitialQueriesRankingToTorcs();
//  addingALatestQueriesRankingToTorcs();
    getRanking();
//  saveARankingAsXLSX();
//  addingAQueryLogger();
//    getSlowestQueryExecutionPlan();

//    getSlowestCTPQueryExecutionPlan();
  }

  private void disableTorcs() {
    // Torcs starts enabled by default
    this.torcs.deactivate();
  }

//  private void enableTorcs() {
//    this.torcs.activate();
//  }
//
//  private void deactivateDefaultObserverTorcs() {
//    // Torcs starts with one observer active (the Default Ranking)
//    this.torcs.getDefaultRanking().deactivate();
//  }
//
//  private void changeDefaultObserverSize() {
//    // Reduce the ranking size to 4 entries. The default size of the default ranking
//    // is 10 (that records the Top 10 slowest queries)
//    // Setting the size of the ranking automatically resets and empties it.
//    this.torcs.getDefaultRanking().setSize(4);
//  }
//
//  private void changeTorcsResetPeriodTo24Hours() {
//    // Changes the reset period of time (minutes). Upon reaching this period of
//    // time, all rankings and observers are reset/emptied. By default all Torcs
//    // observers are reset every 60 minutes.
//    this.torcs.setResetPeriodInMinutes(60 * 24);
//  }
//
//  private void addingAnInitialQueriesRankingToTorcs() {
//    // The following InitialQueriesRanking will keep the first 50 queries ran
//    InitialQueriesRanking iqr = new InitialQueriesRanking(50);
//    this.torcs.register(iqr);
//
//    // After some time the queries will be run the ranking will have recorded them
//    System.out.println("--- " + "Ranking: " + iqr.getTitle() + " Execution Order ---");
//    for (RankingEntry re : iqr.getEntries()) {
//      System.out.println(re);
//    }
//    System.out.println("--- End of Ranking ---");
//
//  }
//
//  private void addingALatestQueriesRankingToTorcs() {
//    // The following LatestQueriesRanking will keep the last 20 queries ran
//    LatestQueriesRanking lqr = new LatestQueriesRanking(20);
//    this.torcs.register(lqr);
//
//    // After some time the queries will be run the ranking will have recorded them
//    System.out.println("--- " + "Ranking: " + lqr.getTitle() + " Execution Order ---");
//    for (RankingEntry re : lqr.getEntries()) {
//      System.out.println(re);
//    }
//    System.out.println("--- End of Ranking ---");
//  }

  @Autowired
  Map<String, DataSource> datasources;

  private void getRanking() {

    System.out.println("--- DataSource beans 2:");
    for (String n : this.datasources.keySet()) {
      DataSource ds = this.datasources.get(n);
      System.out.println("name=" + n + " (" + ds.getClass().getName() + ")");
    }
    System.out.println("--- End of DataSource beans 2");

    BranchTable b = BranchDAO.newTable();
    for (int i = 0; i < 3; i++) {
      List<BranchVO> branches = this.branchDAO.select(b, b.region.ge("NORTH")).execute();
      System.out.println("r[" + branches.size() + "]");
    }
    List<BranchVO> b2 = this.branchDAO.select(b, b.isVip).execute();
    System.out.println("b2=" + b2);
    System.out.println("--- Ranking ---");
    for (RankingEntry re : this.torcs.getDefaultRanking().getEntries()) {
      System.out.println(re);
      try {
        String plan = this.torcs.getEstimatedExecutionPlan(re.getSlowestExecution());
        System.out.println(plan);
      } catch (SQLException | UnsupportedTorcsDatabaseException e) {
        e.printStackTrace();
      }
    }
    System.out.println("--- End of Ranking ---");

  }

//  private void saveARankingAsXLSX() {
//    String xlsxName = "ranking-by-max-response-time.xlsx";
//    try (OutputStream os = new FileOutputStream(new File(xlsxName))) {
//      torcs.getDefaultRanking().saveAsXLSX(os);
//      System.out.println("Ranking saved as: " + xlsxName);
//    } catch (IOException e) {
//      System.out.println("Could not save ranking as XLSX");
//      e.printStackTrace();
//    }
//  }
//
//  private void addingAQueryLogger() {
//    this.torcs.register(new QueryExecutionObserver() {
//
//      @Override
//      public String getTitle() {
//        return "Console Query Logger";
//      }
//
//      @Override
//      public void apply(final QueryExecution sample) {
//        System.out.println("[query] " + sample.getResponseTime() + " ms" + " (exception: "
//            + (sample.getException() == null ? "N/A" : sample.getException().getClass().getName()) + ")" + ": "
//            + QueryExecution.compactSQL(sample.getSQL()));
//      }
//
//      @Override
//      public void reset() {
//        // Nothing to do
//      }
//    });
//
//  }
//
//  private void getSlowestQueryExecutionPlan() throws SQLException, UnsupportedTorcsDatabaseException {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    Random rand = new Random(1234);
//    for (int x = 0; x < 3; x++) { // run three times with different parameters
//      int minAmount = 100 + rand.nextInt(500);
//      sql.select() //
//          .from(i) //
//          .join(b, b.id.eq(i.branchId)) //
//          .where(b.region.eq("SOUTH").and(i.status.ne("UNPAID").and(i.amount.ge(minAmount)))) //
//          .orderBy(i.orderDate.desc()) //
//          .execute();
//    }
//
//    System.out.println("--- Torcs Ranking ---");
//    int pos = 1;
//    for (RankingEntry e : this.torcs.getDefaultRanking().getEntries()) {
//      System.out.println("#" + pos++ + " " + e);
//      String plan = this.torcs.getEstimatedExecutionPlan(e.getSlowestExecution());
//      System.out.println("Execution Plan:\n" + plan);
//    }
//    System.out.println("--- End of Torcs Ranking ---");
//
//  }
//
//  // TODO: Nothing to do, just a marker.
//
//  private void getSlowestCTPQueryExecutionPlan() throws SQLException, UnsupportedTorcsDatabaseException, IOException {
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("b");
//
//    Random rand = new Random(1234);
//    for (int x = 0; x < 3; x++) { // run three times with different parameters
//      int minAmount = 100 + rand.nextInt(500);
//      sql.select() //
//          .from(i) //
//          .join(b, b.id.eq(i.branchId)) //
//          .where(b.region.eq("SOUTH").and(i.status.ne("UNPAID").and(i.amount.ge(minAmount)))) //
//          .orderBy(i.orderDate.desc()) //
//          .execute();
//    }
//
//    System.out.println("--- Torcs Ranking ---");
//    int pos = 1;
//    for (RankingEntry e : this.torcs.getDefaultRanking().getEntries()) {
//      System.out.println("#" + pos++ + " " + e);
//      String plan = this.torcs.getEstimatedExecutionPlan(e.getSlowestExecution(), 2);
//      System.out.println("Execution Plan:\n" + plan);
////      this.torcsCTP.setSegmentSize(180);
////      List<String> ctpPlan = this.torcsCTP.getEstimatedCTPExecutionPlan(e.getSlowestExecution());
////      ctpPlan.stream().forEach(l -> System.out.println("CTP: " + l));
//    }
//    System.out.println("--- End of Torcs Ranking ---");
//
//  }

  private Select<Row> buildRecursiveCTEQuery(final int loops) {
    RecursiveCTE n = sql.recursiveCTE("n", "X");
    n.as(sql.select(sql.literal(1).as("X")),
        sql.select(n.num("X").plus(sql.literal(1))).from(n).where(n.num("X").lt(sql.val(loops))));
    return sql.with(n).select().from(n);
  }

  private void example1InNotIn() {

//    1. IN and NOT IN Operators
//
//    select *
//    from account
//    where branch_id not in (
//      select id from branch where region = 'SOUTH'
//    )

//    AccountTable a = AccountDAO.newTable("a");
//    BranchTable b = BranchDAO.newTable("b");
//
//    Select<Row> q = sql.select() //
//        .from(a) //
//        .where(a.branchId.notIn( //
//            sql.select(b.id).from(b).where(b.region.eq("SOUTH")) //
//        )) //
//        .orderBy(a.branchId, a.parentId.desc(),
//            sql.caseWhen(a.branchId.lt(100), 3).elseValue(0).end().desc().nullsLast());
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example2ExistsNotExists() {

//    2. EXISTS and NOT EXISTS Operators
//
//    select *
//    from account a
//    where not exists (
//      select 1 from branch b where b.id = a.branch_id and b.region = 'SOUTH'
//    )

    AccountTable a = AccountDAO.newTable("a");
    BranchTable b = BranchDAO.newTable("b");

//    Select<Row> q = sql.select() //
//        .from(a) //
//        .where(sql.notExists( //
//            sql.select(sql.val(1)).from(b).where(b.id.eq(a.branchId).and(b.region.eq("SOUTH"))) //
//        ));
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example3AssymmetricOperators() {

//    3. Assymmetric Operators (=, <>, <, >, <=, >= combined with ALL, ANY)
//
//    select * 
//    from invoice i
//    where i.unpaid_balance > any (
//      select amount * 0.5 from invoice x where x.account_id = i.account_id
//    )

    InvoiceTable i = InvoiceDAO.newTable("i");
    InvoiceTable x = InvoiceDAO.newTable("x");

    Select<Row> q = sql.select() //
        .from(i) //
        .where(i.unpaidBalance.gtAny( //
            sql.select(x.amount.mult(0.5)).from(x).where(x.accountId.eq(i.accountId)) //
        ));

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

  }

//  private void example4ScalarSubqueries() {
//
////    4. Scalar Subqueries
////
////    select
////      i.*,
////      50 + (select max(amount) from payment where amount < 1000) / 2 as score, 
////      (select sum(unpaid_balance) from invoice b where b.account_id = i.account_id and b.id <> i.id) as other_unpaid  
////    from invoice i
////    where i.status = 'UNPAID'
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    InvoiceTable b = InvoiceDAO.newTable("b");
//    PaymentTable p = PaymentDAO.newTable("p");
//
//    Select<Row> q = sql.select( //
//        i.star(), //
//        sql.val(50).mult(sql.selectScalar(sql.max(p.amount)).from(p).where(p.amount.lt(1000)).div(2)).as("score"),
//        sql.selectScalar(sql.val("a")).from(b).where(b.accountId.eq(i.accountId).and(b.id.ne(i.id))).substr(1, 3)
//            .as("otherUnpaid"))
//        .from(i) //
//        .where(i.status.eq("UNPAID"));
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));
//
//  }

//  private void example5TableExpressions() {
//
////    5. Table Expressions
////
////    select a.*
////    from account a
////    join (
////      select i.account_id, p.id, sum(l.line_total) as total
////      from invoice i
////      join invoice_line l on l.invoice_id = i.id
////      join product p on p.id = l.product_id
////      where p.type = 'OTC'
////      group by i.account_id, p.id
////    ) x on x.account_id = a.id
////    where x.total > 1000
//
//    AccountTable a = AccountDAO.newTable("a");
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
//    ProductTable p = ProductDAO.newTable("p");
//
//    Subquery x = sql.subquery("x", //
//        sql.select(i.accountId, p.id, sql.sum(l.lineTotal).as("total")) //
//            .from(i) //
//            .join(l, l.invoiceId.eq(i.id)) //
//            .join(p, p.id.eq(l.productId)) //
//            .where(p.type.eq("OTC")) //
//            .groupBy(i.accountId, p.id) //
//    );
//
////    x.materialize("accounting.OTCInvoices");
////    OTCInvoices x =  sql.materializedSubquery("x", "accounting.OTCInvoices", OTCInvoices.class, //
//
//    Select<Row> q = sql.select(a.star()) //
//        .from(a) //
//        .join(x, x.num("accountId").eq(a.id)) //
//        .where(x.num("total").gt(1000));
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));
//
//  }

//  private void example5NestedTableExpressions() {
//
////    5. Nested Table Expressions
////
////    select grp, min(order_date) as start, sum(payment) as group_payment 
////    from (
////      select x.*, sum(inc) over(partition by id order by order_date) as grp
////      from (
////        select i.*, case when type <> lag(type) over(partition by id order by order_date) 
////                         then 1 else 0 end as inc
////        join invoice i 
////        where account_id = 1015 
////      ) x
////    ) y
////    group by grp
//
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
//    ProductTable p = ProductDAO.newTable("p");
//
//    Subquery x = sql.subquery("x", //
//        sql.select( //
//            i.star(), //
//            sql.caseWhen(
//                i.type.ne(sql.lag(i.type, 1, sql.val("a")).over().partitionBy(i.id).orderBy(i.orderDate.asc()).end()),
//                1).elseValue(0).end().as("inc")) //
//            .from(i) //
//            .where(i.accountId.eq(1015)) //
//    );
//    Subquery y = sql.subquery("y", //
//        sql.select( //
//            x.star(), //
//            sql.sum(x.num("inc")).over().partitionBy(x.num("id")).orderBy(x.dt("orderDate").asc()).end().as("grp")) //
//            .from(x) //
//    );
//    Select<Row> q = sql.select( //
//        y.num("grp"), //
//        sql.min(y.dt("orderDate")).as("start"), //
//        sql.sum(y.num("unpaidBalance")).as("groupBalance")) //
//        .from(y) //
//        .groupBy(y.num("grp"));
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));
//
//  }

  private void example5JoinedTableExpressions() {

//    5. Joining Table Expressions
//
//    select x.*
//    from (
//      select b.is_vip, a.* 
//      from branch b
//      join account a on a.branch_id = b.id
//    ) x
//    left join (
//      select i.account_id
//      from invoice i
//      join invoice_line l on l.invoice_id = i.id
//      join product p on p.id = l.product_id
//      where p.shipping = 0
//    ) y on y.account_id = x.id
//    where y.account_id is null

//    BranchTable b = BranchDAO.newTable("b");
//    AccountTable a = AccountDAO.newTable("a");
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
//    ProductTable p = ProductDAO.newTable("p");
//
//    Subquery x = sql.subquery("x", //
//        sql.select(b.isVip, a.star()) //
//            .from(b) //
//            .join(a, a.branchId.eq(b.id)) //
//    );
//    Subquery y = sql.subquery("y", //
//        sql.select(i.accountId) //
//            .from(i) //
//            .join(l, l.invoiceId.eq(i.id)) //
//            .join(p, p.id.eq(l.productId)) //
//            .where(p.shipping.eq(0)));
//    Select<Row> q = sql.select(x.star()) //
//        .from(x) //
//        .leftJoin(y, y.num("accountId").eq(x.num("id"))) //
//        .where(y.num("accountId").isNull());
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example5NamedTableExpressions() {

//    5. Joining Table Expressions
//
//    select x.*
//    from (
//      select b.is_vip, a.* 
//      from branch b
//      join account a on a.branch_id = b.id
//    ) x
//    left join (
//      select i.account_id
//      from invoice i
//      join invoice_line l on l.invoice_id = i.id
//      join product p on p.id = l.product_id
//      where p.shipping = 0
//    ) y (aid) on y.account_id = x.id
//    where y.account_id is null

//    BranchTable b = BranchDAO.newTable("b");
//    AccountTable a = AccountDAO.newTable("a");
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
//    ProductTable p = ProductDAO.newTable("p");
//
//    Subquery x = sql.subquery("x", //
//        sql.select(b.isVip, a.star()) //
//            .from(b) //
//            .join(a, a.branchId.eq(b.id)) //
//    );
//    Subquery y = sql.subquery("y", "ai#d") //
//        .as(sql.select(i.accountId) //
//            .from(i) //
//            .join(l, l.invoiceId.eq(i.id)) //
//            .join(p, p.id.eq(l.productId)) //
//            .where(p.shipping.eq(0)));
//    Select<Row> q = sql.select(x.star()) //
//        .from(x) //
//        .leftJoin(y, y.num("ai#d").eq(x.num("id"))) //
//        .where(y.num("ai#d").isNull());
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example6CTEs() {

//    6. CTEs (Common Table Expressions)
//
//    with
//    x as (
//      select b.vip, a.* 
//      from branch b
//      join account a on a.branch_id = b.id
//    ),
//    y as (
//      select i.account_id
//      from invoice i
//      join invoice_line l on l.account_id = i.id
//      join product p on p.id = l.product_id
//      where p.shipping = 0
//    )
//    select x.*
//    from x
//    left join y on y.account_id = x.id 
//    where y.account_id is null

//    BranchTable b = BranchDAO.newTable("b");
//    AccountTable a = AccountDAO.newTable("a");
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
//    ProductTable p = ProductDAO.newTable("p");
//
//    CTE x = sql.cte("x", //
//        sql.select(b.isVip, a.star()) //
//            .from(b) //
//            .join(a, a.branchId.eq(b.id)) //
//    );
//    CTE y = sql.cte("y", "aid").as(sql.select(i.accountId) //
//        .from(i) //
//        .join(l, l.invoiceId.eq(i.id)) //
//        .join(p, p.id.eq(l.productId)) //
//        .where(p.shipping.eq(0)));
//    Select<Row> q = sql.with(x, y) //
//        .select(x.star()) //
//        .from(x) //
//        .leftJoin(y, y.num("aid").eq(x.num("id"))) //
//        .where(y.num("aid").isNull());
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example7RecursiveCTEs() {

//  7. Recursive CTEs -- Pending
//
    // with recursive
    // g as (
    // select id from account where id = 1215
    // union all
    // select b.id
    // from g
    // join account b on b.parent_id = g.id
    // )
    // select g.id, i.amount
    // from g
    // join invoice i on i.account_id = g.id;

//    AccountTable a = AccountDAO.newTable("a");
//    AccountTable b = AccountDAO.newTable("b");
//    InvoiceTable i = InvoiceDAO.newTable("i");
//
//    RecursiveCTE g = sql.recursiveCTE("g", "id");
//    g.as( //
//        sql.select(a.id) //
//            .from(a) //
//            .where(a.id.eq(1215)) //
//        , //
//        sql.select(b.id) //
//            .from(g) //
//            .join(b, b.parentId.eq(g.num("id"))) //
//            .where(b.parentId.eq(g.num("id"))) //
//    );
//
//    Select<Row> q = sql.with(g) //
//        .select(g.num("id"), i.amount) //
//        .from(g) //
//        .join(i, i.accountId.eq(g.num("id")));
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example8LateralJoins() {

//    8. Lateral Joins
//
//    select *
//    from account a
//    join lateral (
//      select order_date
//      from invoice i
//      where i.account_id = a.id
//      order by order_date desc
//      limit 2
//    ) x on true
//    left join lateral (
//      select payment_date as last_payment_date
//      from payment p
//      where p.invoice_id = i.id
//      order by payment_date desc
//      limit 1
//    ) y on true
//    where a.branch_id = 1014

//    AccountTable a = AccountDAO.newTable("a");
//    InvoiceTable i = InvoiceDAO.newTable("i");
//    PaymentTable p = PaymentDAO.newTable("p");
//
//    Subquery x = sql.subquery("x", //
//        sql.select(i.orderDate) //
//            .from(i) //
//            .where(i.accountId.eq(a.id)) //
//            .orderBy(i.orderDate.desc()) //
//            .limit(2) //
//    );
//    Subquery y = sql.subquery("y", //
//        sql.select(p.paymentDate.as("lastPayment")) //
//            .from(p) //
//            .where(p.invoiceId.eq(a.id)) //
//            .orderBy(p.paymentDate.desc()) //
//            .limit(1) //
//    );
//    Select<Row> q = sql.select() //
//        .from(a) //
//        .joinLateral(x) //
//        .leftJoinLateral(y) //
//        .where(a.branchId.eq(1014));
//
//    System.out.println(q.getPreview());
//    q.execute().forEach(r -> System.out.println("row: " + r));

  }

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

//    InvoiceTable i = InvoiceDAO.newTable("i");
//    BranchTable b = BranchDAO.newTable("c");
//
//    SelectFromPhase<Row> q = this.sql.select( //
//        i.id, i.amount.as("totalAmount"), // declared columns w/wo alias
//        i.star(), // simple *
//        b.star().filter(c -> true), // filtered *
//        b.star().as(c -> "b#" + c.getProperty()) // aliased *
//    ) //
//        .from(i) //
//        .join(b, b.id.eq(i.branchId));
//    System.out.println("q:" + q.getPreview());
//    List<Row> rows = q.execute();
//    for (Row r : rows) {
//      System.out.println("r=" + r);
//    }

  }

  private void selectByCriteria() {
//    reinsert();

    InvoiceTable i = InvoiceDAO.newTable("i");

    EntitySelect<InvoiceVO> q = this.invoiceDAO.select(i, i.amount.gt(1));
    System.out.println("q=" + q.getPreview());

    for (InvoiceVO r : q.execute()) {
      System.out.println("r=" + r);
    }

  }

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
