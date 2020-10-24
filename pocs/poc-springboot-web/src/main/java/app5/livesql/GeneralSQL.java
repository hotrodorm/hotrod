package app5.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app5.persistence.primitives.AccountDAO;
import app5.persistence.primitives.AccountDAO.AccountTable;

@Component("generalSQLExamples")
public class GeneralSQL {

  @Autowired
  private LiveSQL sql;

  public void select() {

    // SELECT 123, 'Chicago'

    List<Map<String, Object>> rows = sql //
        .select(sql.val(123), sql.val("Chicago")) //
        .execute() //
    ;

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void showSQLStatement() {

    // 1. Assemble the SQL statement
    ExecutableSelect<Map<String, Object>> ps = sql //
        .select( //
            sql.val(123).as("balance"), //
            sql.val(456.78).as("deposit"), //
            sql.val("Chicago").as("city"), //
            sql.currentDate().as("performedAt"));

    // 2. Show the SQL statement and parameters
    System.out.print(ps);
    // In PostgreSQL it's rendered as:
    // --- SQL ---
    // SELECT
    // 123 as balance,
    // #{p1} as deposit,
    // 'Chicago' as city,
    // current_date as "performedAt"
    // --- Parameters ---
    // * p1 (java.lang.Double): 456.78
    // ------------------

    // 3. Run the SQL statement
    List<Map<String, Object>> rows = ps.execute();

    // 4. Display the result set
    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }
    // In PostgreSQL it shows:
    // row: {balance=123, city=Chicago, performedAt=2019-07-19, deposit=456.78}

  }

  public void selectFrom() {

    // SELECT *
    // FROM account a

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select() //
        .from(a) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void selectDistinctFrom() {

    // SELECT distinct *
    // FROM account a

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .selectDistinct() //
        .from(a) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void selectColumnsFrom() {

    // SELECT a.name, a.created_on
    // FROM account a

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select(a.name, a.createdOn) //
        .from(a) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void where() {

    // SELECT *
    // FROM account a
    // WHERE current_balance >= 150.0 and active = 1

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select() //
        .from(a) //
        .where(a.currentBalance.ge(150.0).and(a.active.eq(1))) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void groupBy() {

    // SELECT a.type, sum(current_balance) as "totalBalance"
    // FROM account a
    // WHERE current_balance >= 150.0 and active = 1
    // GROUP BY a.type

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select(a.type, sql.sum(a.currentBalance).as("totalBalance")) //
        .from(a) //
        .where(a.currentBalance.ge(150.0).and(a.active.eq(1))) //
        .groupBy(a.type) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void having() {

    // SELECT a.type, sum(current_balance) as "totalBalance"
    // FROM account a
    // WHERE current_balance >= 150.0 and active = 1
    // GROUP BY a.type
    // HAVING sum(current_balance) >= 1000

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select(a.type, sql.sum(a.currentBalance).as("totalBalance")) //
        .from(a) //
        .where(a.currentBalance.ge(150.0).and(a.active.eq(1))) //
        .groupBy(a.type) //
        .having(sql.sum(a.currentBalance).ge(1000)) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void orderBy() {

    // SELECT a.type, sum(current_balance) as "totalBalance"
    // FROM account a
    // WHERE current_balance >= 150.0 and active = 1
    // GROUP BY a.type
    // HAVING sum(current_balance) >= 1000
    // ORDER BY a.type

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select(a.type, sql.sum(a.currentBalance).as("totalBalance")) //
        .from(a) //
        .where(a.currentBalance.ge(150.0).and(a.active.eq(1))) //
        .groupBy(a.type) //
        .having(sql.sum(a.currentBalance).ge(1000)) //
        .orderBy(a.type.asc()) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void offset() {

    // SELECT a.type, sum(current_balance) as "totalBalance"
    // FROM account a
    // WHERE current_balance >= 150.0 and active = 1
    // GROUP BY a.type
    // HAVING sum(current_balance) >= 1000
    // ORDER BY a.type
    // OFFSET 300

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select(a.type, sql.sum(a.currentBalance).as("totalBalance")) //
        .from(a) //
        .where(a.currentBalance.ge(150.0).and(a.active.eq(1))) //
        .groupBy(a.type) //
        .having(sql.sum(a.currentBalance).ge(1000)) //
        .orderBy(a.type.asc()) //
        .offset(300) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void limit() {

    // SELECT a.type, sum(current_balance) as "totalBalance"
    // FROM account a
    // WHERE current_balance >= 150.0 and active = 1
    // GROUP BY a.type
    // HAVING sum(current_balance) >= 1000
    // ORDER BY a.type
    // OFFSET 300
    // LIMIT 50

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select(a.type, sql.sum(a.currentBalance).as("totalBalance")) //
        .from(a) //
        .where(a.currentBalance.ge(150.0).and(a.active.eq(1))) //
        .groupBy(a.type) //
        .having(sql.sum(a.currentBalance).ge(1000)) //
        .orderBy(a.type.asc()) //
        .offset(300) //
        .limit(50) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void caseExpressions() {

    // SELECT
    // a.id,
    // case when a.type = 'CHK' then 3.5 else 1.0 end,
    // case when a.type = 'CHK' then 3.5 else 1.0 end + 1 as factor
    // FROM account a
    // ORDER BY case
    // when a.type = 'INV' then 8.0
    // when a.current_balance > 100 then 1.7
    // else 0.5 end desc,
    // a.current_balance

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select(a.id, //
            sql.caseWhen(a.type.eq("CHK"), 3.5).elseValue(1.0).end(), //
            sql.caseWhen(a.type.eq("CHK"), 3.5).elseValue(1.0).end().plus(1) //
                .as("factor")) //
        .from(a) //
        .orderBy(sql.caseWhen(a.type.eq("INV"), 8.0).when(a.currentBalance.gt(100), 1.7).elseValue(0.5).end().desc(),
            a.currentBalance.asc()) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

}
