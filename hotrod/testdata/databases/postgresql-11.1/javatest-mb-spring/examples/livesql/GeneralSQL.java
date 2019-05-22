package examples.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;

@Service("generalSQLExamples")
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

  public void selectFrom() {

    // SELECT *
    // FROM account a

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select() //
        .from(a) //
        .execute() //
    ;

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
        .execute() //
    ;

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
        .execute() //
    ;

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
        .where(a.currentBalance.ge(150.0).and(a.mainStatus.eq(1))) //
        .execute() //
    ;

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
        .where(a.currentBalance.ge(150.0).and(a.mainStatus.eq(1))) //
        .groupBy(a.type) //
        .execute() //
    ;

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
        .where(a.currentBalance.ge(150.0).and(a.mainStatus.eq(1))) //
        .groupBy(a.type) //
        .having(sql.sum(a.currentBalance).ge(1000)) //
        .execute() //
    ;

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
        .where(a.currentBalance.ge(150.0).and(a.mainStatus.eq(1))) //
        .groupBy(a.type) //
        .having(sql.sum(a.currentBalance).ge(1000)) //
        .orderBy(a.type.asc()) //
        .execute() //
    ;

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
        .where(a.currentBalance.ge(150.0).and(a.mainStatus.eq(1))) //
        .groupBy(a.type) //
        .having(sql.sum(a.currentBalance).ge(1000)) //
        .orderBy(a.type.asc()) //
        .offset(300) //
        .execute() //
    ;

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
        .where(a.currentBalance.ge(150.0).and(a.mainStatus.eq(1))) //
        .groupBy(a.type) //
        .having(sql.sum(a.currentBalance).ge(1000)) //
        .orderBy(a.type.asc()) //
        .offset(300) //
        .limit(50) //
        .execute() //
    ;

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void caseExpressions() {

    // SELECT a.type, case when a.type = 'CHK' then 3.5 else 1.0 end as "factor"
    // FROM account a
    // ORDER BY case 
    // when a.type = 'INV' then 8.0 
    // when a.current_balance > 100 then 1.7
    // else 0.5 end 
    // desc

    AccountTable a = AccountDAO.newTable("a");

    List<Map<String, Object>> rows = sql //
        .select(a.id, sql.caseWhen(a.type.eq("CHK"), 3.5).elseValue(1.0).end().as("factor")) //
        .from(a) //
        .orderBy(sql.caseWhen(a.type.eq("INV"), 8.0).when(a.currentBalance.gt(100), 1.7).elseValue(0.5).end().desc()) //
        .execute() //
    ;

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

}
