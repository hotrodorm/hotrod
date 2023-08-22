package app;

import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.ctes.RecursiveCTE;
import org.hotrod.runtime.livesql.queries.select.ExecutableCriteriaSelect;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;
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
import org.springframework.context.annotation.PropertySource;

import app.daos.InvoiceVO;
import app.daos.primitives.AccountDAO;
import app.daos.primitives.AccountDAO.AccountTable;
import app.daos.primitives.BranchDAO;
import app.daos.primitives.BranchDAO.BranchTable;
import app.daos.primitives.InvoiceDAO;
import app.daos.primitives.InvoiceDAO.InvoiceTable;
import app.daos.primitives.InvoiceLineDAO;
import app.daos.primitives.InvoiceLineDAO.InvoiceLineTable;
import app.daos.primitives.PaymentDAO;
import app.daos.primitives.PaymentDAO.PaymentTable;
import app.daos.primitives.ProductDAO;
import app.daos.primitives.ProductDAO.ProductTable;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@MapperScan("mappers")
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
@PropertySource(value = { "file:application.properties",
    "classpath:application.properties" }, ignoreResourceNotFound = true)
public class App {

//  @Autowired
//  private NumbersDAO numbersDAO;

//  @Autowired
//  private BranchDAO branchDAO;
//
//  @Autowired
//  private AccountDAO accountDAO;

  @Autowired
  private InvoiceDAO invoiceDAO;

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

  }
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

  private void liveSQLExamples() {
    example1InNotIn();
    example2ExistsNotExists();
    example3AssymmetricOperators();
    example4ScalarSubqueries();
    example5TableExpressions();
    example5NestedTableExpressions();
    example5JoinedTableExpressions();
    example5NamedTableExpressions();
    example6CTEs();
    example7RecursiveCTEs();
    example8LateralJoins();
  }

  private void example1InNotIn() {

//    1. IN and NOT IN Operators
//
//    select *
//    from account
//    where branch_id not in (
//      select id from branch where region = 'SOUTH'
//    )

    AccountTable a = AccountDAO.newTable("a");
    BranchTable b = BranchDAO.newTable("b");

    ExecutableSelect<Row> q = sql.select() //
        .from(a) //
        .where(a.branchId.notIn( //
            sql.select(b.id).from(b).where(b.region.eq("SOUTH")) //
        ));

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

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

    ExecutableSelect<Row> q = sql.select() //
        .from(a) //
        .where(sql.notExists( //
            sql.select(sql.val(1)).from(b).where(b.id.eq(a.branchId).and(b.region.eq("SOUTH"))) //
        ));

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

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

    ExecutableSelect<Row> q = sql.select() //
        .from(i) //
        .where(i.unpaidBalance.gtAny( //
            sql.select(x.amount.mult(0.5)).from(x).where(x.accountId.eq(i.accountId)) //
        ));

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example4ScalarSubqueries() {

//    4. Scalar Subqueries
//
//    select
//      i.*,
//      50 + (select max(amount) from payment where amount < 1000) / 2 as score, 
//      (select sum(unpaid_balance) from invoice b where b.account_id = i.account_id and b.id <> i.id) as other_unpaid  
//    from invoice i
//    where i.status = 'UNPAID'

    InvoiceTable i = InvoiceDAO.newTable("i");
    InvoiceTable b = InvoiceDAO.newTable("b");
    PaymentTable p = PaymentDAO.newTable("p");

    ExecutableSelect<Row> q = sql.select( //
        i.star(), //
        sql.val(50).mult(sql.selectScalar(sql.max(p.amount)).from(p).where(p.amount.lt(1000)).div(2)).as("score"),
        sql.selectScalar(sql.val("a")).from(b).where(b.accountId.eq(i.accountId).and(b.id.ne(i.id))).substr(1, 3)
            .as("otherUnpaid"))
        .from(i) //
        .where(i.status.eq("UNPAID"));
    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example5TableExpressions() {

//    5. Table Expressions
//
//    select a.*
//    from account a
//    join (
//      select i.account_id, p.id, sum(l.line_total) as total
//      from invoice i
//      join invoice_line l on l.invoice_id = i.id
//      join product p on p.id = l.product_id
//      where p.type = 'OTC'
//      group by i.account_id, p.id
//    ) x on x.account_id = a.id
//    where x.total > 1000

    AccountTable a = AccountDAO.newTable("a");
    InvoiceTable i = InvoiceDAO.newTable("i");
    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
    ProductTable p = ProductDAO.newTable("p");

    Subquery x = sql.subquery("x", //
        sql.select(i.accountId, p.id, sql.sum(l.lineTotal).as("total")) //
            .from(i) //
            .join(l, l.invoiceId.eq(i.id)) //
            .join(p, p.id.eq(l.productId)) //
            .where(p.type.eq("OTC")) //
            .groupBy(i.accountId, p.id) //
    );

//    x.materialize("accounting.OTCInvoices");
//    OTCInvoices x =  sql.materializedSubquery("x", "accounting.OTCInvoices", OTCInvoices.class, //

    ExecutableSelect<Row> q = sql.select(a.star()) //
        .from(a) //
        .join(x, x.num("accountId").eq(a.id)) //
        .where(x.num("total").gt(1000));

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

  }

  private void example5NestedTableExpressions() {

//    5. Nested Table Expressions
//
//    select grp, min(order_date) as start, sum(payment) as group_payment 
//    from (
//      select x.*, sum(inc) over(partition by id order by order_date) as grp
//      from (
//        select i.*, case when type <> lag(type) over(partition by id order by order_date) 
//                         then 1 else 0 end as inc
//        join invoice i 
//        where account_id = 1015 
//      ) x
//    ) y
//    group by grp

    InvoiceTable i = InvoiceDAO.newTable("i");
    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
    ProductTable p = ProductDAO.newTable("p");

    Subquery x = sql.subquery("x", //
        sql.select( //
            i.star(), //
            sql.caseWhen(
                i.type.ne(sql.lag(i.type, 1, sql.val("a")).over().partitionBy(i.id).orderBy(i.orderDate.asc()).end()),
                1).elseValue(0).end().as("inc")) //
            .from(i) //
            .where(i.accountId.eq(1015)) //
    );
    Subquery y = sql.subquery("y", //
        sql.select( //
            x.star(), //
            sql.sum(x.num("inc")).over().partitionBy(x.num("id")).orderBy(x.dt("orderDate").asc()).end().as("grp")) //
            .from(x) //
    );
    ExecutableSelect<Row> q = sql.select( //
        y.num("grp"), //
        sql.min(y.dt("orderDate")).as("start"), //
        sql.sum(y.num("unpaidBalance")).as("groupBalance")) //
        .from(y) //
        .groupBy(y.num("grp"));

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

  }

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

    BranchTable b = BranchDAO.newTable("b");
    AccountTable a = AccountDAO.newTable("a");
    InvoiceTable i = InvoiceDAO.newTable("i");
    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
    ProductTable p = ProductDAO.newTable("p");

    Subquery x = sql.subquery("x", //
        sql.select(b.isVip, a.star()) //
            .from(b) //
            .join(a, a.branchId.eq(b.id)) //
    );
    Subquery y = sql.subquery("y", //
        sql.select(i.accountId) //
            .from(i) //
            .join(l, l.invoiceId.eq(i.id)) //
            .join(p, p.id.eq(l.productId)) //
            .where(p.shipping.eq(0)));
    ExecutableSelect<Row> q = sql.select(x.star()) //
        .from(x) //
        .leftJoin(y, y.num("accountId").eq(x.num("id"))) //
        .where(y.num("accountId").isNull());

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

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

    BranchTable b = BranchDAO.newTable("b");
    AccountTable a = AccountDAO.newTable("a");
    InvoiceTable i = InvoiceDAO.newTable("i");
    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
    ProductTable p = ProductDAO.newTable("p");

    Subquery x = sql.subquery("x", //
        sql.select(b.isVip, a.star()) //
            .from(b) //
            .join(a, a.branchId.eq(b.id)) //
    );
    Subquery y = sql.subquery("y", "ai#d") //
        .as(sql.select(i.accountId) //
            .from(i) //
            .join(l, l.invoiceId.eq(i.id)) //
            .join(p, p.id.eq(l.productId)) //
            .where(p.shipping.eq(0)));
    ExecutableSelect<Row> q = sql.select(x.star()) //
        .from(x) //
        .leftJoin(y, y.num("ai#d").eq(x.num("id"))) //
        .where(y.num("ai#d").isNull());

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

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

    BranchTable b = BranchDAO.newTable("b");
    AccountTable a = AccountDAO.newTable("a");
    InvoiceTable i = InvoiceDAO.newTable("i");
    InvoiceLineTable l = InvoiceLineDAO.newTable("l");
    ProductTable p = ProductDAO.newTable("p");

    CTE x = sql.cte("x", //
        sql.select(b.isVip, a.star()) //
            .from(b) //
            .join(a, a.branchId.eq(b.id)) //
    );
    CTE y = sql.cte("y", "aid").as(sql.select(i.accountId) //
        .from(i) //
        .join(l, l.invoiceId.eq(i.id)) //
        .join(p, p.id.eq(l.productId)) //
        .where(p.shipping.eq(0)));
    ExecutableSelect<Row> q = sql.with(x, y) //
        .select(x.star()) //
        .from(x) //
        .leftJoin(y, y.num("aid").eq(x.num("id"))) //
        .where(y.num("aid").isNull());

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

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

    AccountTable a = AccountDAO.newTable("a");
    AccountTable b = AccountDAO.newTable("b");
    InvoiceTable i = InvoiceDAO.newTable("i");

    RecursiveCTE g = sql.recursiveCTE("g", "id");
    g.as( //
        sql.select(a.id) //
            .from(a) //
            .where(a.id.eq(1215)) //
        , //
        sql.select(b.id) //
            .from(g) //
            .join(b, b.parentId.eq(g.num("id"))) //
            .where(b.parentId.eq(g.num("id"))) //
    );

    ExecutableSelect<Row> q = sql.with(g) //
        .select(g.num("id"), i.amount) //
        .from(g) //
        .join(i, i.accountId.eq(g.num("id")));

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

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

    AccountTable a = AccountDAO.newTable("a");
    InvoiceTable i = InvoiceDAO.newTable("i");
    PaymentTable p = PaymentDAO.newTable("p");

    Subquery x = sql.subquery("x", //
        sql.select(i.orderDate) //
            .from(i) //
            .where(i.accountId.eq(a.id)) //
            .orderBy(i.orderDate.desc()) //
            .limit(2) //
    );
    Subquery y = sql.subquery("y", //
        sql.select(p.paymentDate.as("lastPayment")) //
            .from(p) //
            .where(p.invoiceId.eq(a.id)) //
            .orderBy(p.paymentDate.desc()) //
            .limit(1) //
    );
    ExecutableSelect<Row> q = sql.select() //
        .from(a) //
        .joinLateral(x) //
        .leftJoinLateral(y) //
        .where(a.branchId.eq(1014));

    System.out.println(q.getPreview());
    q.execute().forEach(r -> System.out.println("row: " + r));

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

    ExecutableCriteriaSelect<InvoiceVO> q = this.invoiceDAO.select(i, i.amount.gt(1));
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
