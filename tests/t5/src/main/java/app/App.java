package app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.cursors.Cursor;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.assembler.QueryAssembler;
import org.hotrod.livesql.Row;
import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.predicates.converter.ConvertedColumn;
import org.hotrod.runtime.livesql.expressions.predicates.converter.ConvertedEqual;
import org.hotrod.runtime.livesql.expressions.predicates.converter.ConvertedIn;
import org.hotrod.runtime.livesql.expressions.predicates.converter.ConvertedNotEqual;
import org.hotrod.runtime.livesql.expressions.predicates.converter.ConvertedNotIn;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderByDirectionPhase;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.TypeSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.AccountTypeConverter.AccountType;
import app.persistence.dao.AccountDAO;
import app.persistence.dao.AccountDAO.AccountBaseline;
import app.persistence.dao.AccountDAO.AccountTable;
import app.persistence.model.Account;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@ComponentScan(basePackageClasses = QueryAssembler.class)
@ComponentScan(basePackageClasses = AccountDAO.class)
public class App {

  private static final Logger log = Logger.getLogger(App.class.getName());

  static {
//    JULCustomFormatter.initialize(Level.FINER);
  }

  @Autowired
  private AccountDAO accountDAO;

//  @Autowired
//  private ADAO aDAO;

//  @Autowired
//  private ReportingDAO reportingDAO;

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
//      testLiveSQLCursor();
//      testConverter5();
      testConverter6();
//      testOptimisticLocking();
      log.info("[ Ending ]");
    };
  }

  private void testOptimisticLocking() throws SQLException, DynamicExpressionException {
//    testOLInsert();
//  testOLInsertByExample() ;
//    testOLUpdate();
//    testOLDelete();
//  testOLDeleteDelete();
//    testOLDeleteUpdate();
    testOLUpdateDelete();
//    testOLUpdateUpdate();
  }

  private void testOLInsert() throws DynamicExpressionException, SQLException {
    Account a = new Account();
    a.setName("1010-4");
    a.setType(AccountType.CHK);
    a.setBalance(100);
//    a.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
//    a.setActive(true);
//    a.setVersion(1);
    this.accountDAO.insert(a);
    System.out.println("--> a=" + a);

    Account b = this.accountDAO.select(a.getId());
    System.out.println("b=" + b);
  }

  private void testOLInsertByExample() throws DynamicExpressionException, SQLException {
    Account a = new Account();
    a.setName("1010-4");
    a.setType(AccountType.CHK);
    a.setBalance(100);
//    a.setActive(true);
//    a.setVersion(1);
    this.accountDAO.insertByExample(a);
    System.out.println("--> a=" + a);

    Account b = this.accountDAO.select(a.getId());
    System.out.println("b=" + b);
  }

  private void testOLUpdate() throws DynamicExpressionException, SQLException {
    Account a = this.accountDAO.select(112);
    System.out.println("--> a=" + a);
    AccountBaseline baseline = this.accountDAO.baseline(a);
    a.setBalance(a.getBalance() + 100);
//    this.accountDAO.update(a, baseline);
    System.out.println("Updated a.");
  }

  private void testOLDelete() throws DynamicExpressionException, SQLException {
    Account a = this.accountDAO.select(112);
    AccountBaseline baseline = this.accountDAO.baseline(a);
    System.out.println("--> a=" + a);
//    this.accountDAO.delete(baseline);
    System.out.println("Updated a.");
  }

  private void testOLDeleteUpdate() throws DynamicExpressionException, SQLException {
    Account b = this.accountDAO.select(112);

    Account a = this.accountDAO.select(112);
    System.out.println("--> a=" + a);
    AccountBaseline baseline = this.accountDAO.baseline(a);

    this.accountDAO.delete(b);
    System.out.println("Deleted b.");

    a.setBalance(a.getBalance() + 100);
//    this.accountDAO.update(a, baseline);
    System.out.println("Updated a.");
  }

  private void testOLUpdateDelete() throws DynamicExpressionException, SQLException {
    Account b = this.accountDAO.select(112);

    Account a = this.accountDAO.select(112);
    System.out.println("--> a=" + a);
    AccountBaseline baseline = this.accountDAO.baseline(a);

    a.setBalance(a.getBalance() + 100);
//    this.accountDAO.update(a, baseline);
    System.out.println("Updated a.");

    this.accountDAO.delete(b);
    System.out.println("Deleted b.");
  }

  private void testConverter6() throws SQLException, DynamicExpressionException, IOException {

    AccountTable a = this.accountDAO.newTable();

    TypeHandler th = TypeHandler.of(IntegerBooleanConverter.class, TypeSource.ENTITY_COLUMN);
    @SuppressWarnings("unchecked")
    TypeConverter<Integer, Boolean> converter = (TypeConverter<Integer, Boolean>) th.getConverter();
    OneConvertedColumn<Integer, Boolean> dactive = new OneConvertedColumn<Integer, Boolean>(a, "ACTIVE", "active",
        "INTEGER", 32, 0, TypeHandler.of(IntegerBooleanConverter.class, TypeSource.ENTITY_COLUMN), converter);

    TypeHandler th2 = TypeHandler.of(AccountTypeConverter.class, TypeSource.ENTITY_COLUMN);
    @SuppressWarnings("unchecked")
    TypeConverter<String, AccountType> converter2 = (TypeConverter<String, AccountType>) th2.getConverter();
    OneConvertedColumn<String, AccountType> dtype = new OneConvertedColumn<String, AccountType>(a, "TYPE", "type",
        "VARCHAR", 3, 0, TypeHandler.of(IntegerBooleanConverter.class, TypeSource.ENTITY_COLUMN), converter2);

//    SelectWherePhase<Row> q = this.sql.select().from(a).where(dactive.eq(true));
//    SelectWherePhase<Row> q = this.sql.select().from(a).where(dtype.ne(AccountType.CHK));
    Select<Row> q = this.sql.select().from(a).where(dtype.notIn(AccountType.CHK, AccountType.INV)).orderBy(dactive);
//    Select<Row> q = this.sql.select().from(a).where(dtype.notIn(AccountType.CHK, AccountType.INV)).orderBy(a.balance);
    System.out.println("query:\n" + q.getPreview(true));
    List<Row> rows = q.execute();

    for (Row r : rows) {
      System.out.println("Row=" + r);
    }
  }

  public class OneConvertedColumn<R, D> extends ConvertedColumn<R, D> {

    private String type;
    private Integer columnSize;
    private Integer decimalDigits;
    private String property;
    private TypeConverter<R, D> converter;

    public OneConvertedColumn(final TableOrView objectInstance, final String name, final String property,
        final String type, final Integer columnSize, final Integer decimalDigits, final TypeHandler handler,
        final TypeConverter<R, D> converter) {
      super(Expression.PRECEDENCE_COLUMN, objectInstance, name);
      this.property = property;
      this.type = type;
      this.columnSize = columnSize;
      this.decimalDigits = decimalDigits;
      super.setTypeHandler(handler);
      this.converter = converter;
    }

//    public abstract E coalesce(final D d);
//
//    public abstract E nullIf(final D d);

    public Predicate eq(final D d) {
      return new ConvertedEqual<R, D>(this, d, this.converter);
    }

    public Predicate ne(final D d) {
      return new ConvertedNotEqual<R, D>(this, d, this.converter);
    }

    public Predicate in(final D... d) {
      return new ConvertedIn<R, D>(this, this.converter, d);
    }

    public Predicate notIn(final D... d) {
      return new ConvertedNotIn<R, D>(this, this.converter, d);
    }

//    public AliasedExpression as(final String alias) {
//      if (SUtil.isEmpty(alias)) {
//        throw new LiveSQLException("An alias specified with the .as() method cannot be null");
//      }
//      return new AliasedExpression(this, alias);
//    }

  }

//  private void testConverter5() throws SQLException, DynamicExpressionException, IOException {
//    ATable a = this.aDAO.newTable();
//
//    System.out.println(">> query 1");
//
//    Select<Row> q = this.sql.select(a.star()).from(a)
////        .where(a.esActivo.eq(true))
//    ;
//    System.out.println(">> q:" + q.getPreview());
//
//    List<Row> lista = q.execute();
//
//    System.out.println(">> A:");
//    lista.forEach(r -> System.out.println(r));
//
//  }

  private void testLiveSQLCursor() throws SQLException, DynamicExpressionException, IOException {

//    try (Cursor<Row> rows = this.sql.select(sql.val(7).mult(3).as("answer"), sql.ONE.as("one"))
//        .executeCursor(60);) {
//      System.out.println(">> Rows:");
//      rows.forEach(r -> System.out.println(r));
//    }

    AccountTable a = this.accountDAO.newTable();
    try (Cursor<Account> accounts = this.accountDAO.select(a, sql.TRUE).executeCursor(60);) {
      System.out.println(">> Accounts:");
      accounts.forEach(r -> System.out.println(r));
    }

  }

  private void testLiveSQL() throws SQLException, DynamicExpressionException {

    Account ax = this.accountDAO.select(123);
    System.out.println(">> Account 123: " + ax);

//    AccountTable a = this.accountDAO.newTable();
//    List<Account> accounts = this.accountDAO.select(a, a.balance.gt(500)).execute();
//    System.out.println(">> Accounts:");
//    accounts.forEach(r -> System.out.println(r));

//    Row row = this.sql.select(sql.val(7).mult(3).as("answer")).executeOne();
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
