package app5.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app5.persistence.primitives.AccountDAO;
import app5.persistence.primitives.AccountDAO.AccountTable;
import app5.persistence.primitives.FederalBranchDAO;
import app5.persistence.primitives.FederalBranchDAO.FederalBranchTable;
import app5.persistence.primitives.TransactionDAO;
import app5.persistence.primitives.TransactionDAO.TransactionTable;

@Component("joinsExamples")
public class Joins {

  @Autowired
  private LiveSQL sql;

  public void innerJoin() {

    // SELECT a.id, a.name as "accountName", t.amount, b.name as "branchName"
    // FROM account a
    // JOIN transaction t on t.account_id = a.id
    // JOIN federal_branch b on b.id = t.fed_branch_id
    // WHERE a.type = 'CHK'

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");
    FederalBranchTable b = FederalBranchDAO.newTable("b");

    List<Map<String, Object>> rows = sql //
        .select(a.id, a.name.as("accountName"), t.amount, b.name.as("branchName")) //
        .from(a) //
        .join(t, t.accountId.eq(a.id)) //
        .join(b, b.id.eq(t.fedBranchId)) //
        .where(a.type.eq("CHK")) // ... and rest of SQL
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void innerJoinUsing() {

    // SELECT a.id, a.name as "accountName", t.amount, b.name as "branchName"
    // FROM account a
    // JOIN federal_branch b USING (id)
    // WHERE a.type = 'CHK'

    AccountTable a = AccountDAO.newTable("a");
    FederalBranchTable b = FederalBranchDAO.newTable("b");

    List<Map<String, Object>> rows = sql //
        .select(a.id, a.name.as("accountName"), b.name.as("branchName")) //
        .from(a) //
        .join(b, b.id) //
        .where(a.type.eq("CHK")) // ... and rest of SQL
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void leftOuterJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // LEFT JOIN transaction t on t.account_id = a.id

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .leftJoin(t, t.accountId.eq(a.id)) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void leftOuterJoinUsing() {

    // SELECT a.id, a.name as "accountName", t.amount, b.name as "branchName"
    // FROM account a
    // LEFT JOIN federal_branch b USING (id)
    // WHERE a.type = 'CHK'

    AccountTable a = AccountDAO.newTable("a");
    FederalBranchTable b = FederalBranchDAO.newTable("b");

    List<Map<String, Object>> rows = sql //
        .select(a.id, a.name.as("accountName"), b.name.as("branchName")) //
        .from(a) //
        .leftJoin(b, b.id) //
        .where(a.type.eq("CHK")) // ... and rest of SQL
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void rightOuterJoin() {

    // SELECT a.id, t.amount
    // FROM transaction t
    // RIGHT JOIN account a on t.account_id = a.id

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(t) //
        .rightJoin(a, t.accountId.eq(a.id)) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void rightOuterJoinUsing() {

    // SELECT a.id, a.name as "accountName", t.amount, b.name as "branchName"
    // FROM account a
    // JOIN federal_branch b USING (id)
    // WHERE a.type = 'CHK'

    AccountTable a = AccountDAO.newTable("a");
    FederalBranchTable b = FederalBranchDAO.newTable("b");

    List<Map<String, Object>> rows = sql //
        .select(a.id, a.name.as("accountName"), b.name.as("branchName")) //
        .from(a) //
        .rightJoin(b, b.id) //
        .where(a.type.eq("CHK")) // ... and rest of SQL
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void fullOuterJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // FULL OUTER JOIN transaction t on t.account_id = a.id

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .fullJoin(t, t.accountId.eq(a.id)) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void fullOuterJoinUsing() {

    // SELECT a.id, a.name as "accountName", t.amount, b.name as "branchName"
    // FROM account a
    // JOIN federal_branch b USING (id)
    // WHERE a.type = 'CHK'

    AccountTable a = AccountDAO.newTable("a");
    FederalBranchTable b = FederalBranchDAO.newTable("b");

    List<Map<String, Object>> rows = sql //
        .select(a.id, a.name.as("accountName"), b.name.as("branchName")) //
        .from(a) //
        .fullJoin(b, b.id) //
        .where(a.type.eq("CHK")) // ... and rest of SQL
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void crossJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // CROSS JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .crossJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void naturalInnerJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // NATURAL JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .naturalJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void naturalLeftOuterJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // NATURAL LEFT JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .naturalLeftJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void naturalRightOuterJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // NATURAL RIGHT JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .naturalRightJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void naturalFullOuterJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // NATURAL FULL JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .naturalFullJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void unionJoin() {

    // Only HyperSQL supports (afaik) this SQL standard clause
    // The UNION JOIN was added to the SQL standard in SQL-92 and removed from it in
    // SQL:2003

    // SELECT a.id, t.amount
    // FROM account a
    // UNION JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .unionJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

}
