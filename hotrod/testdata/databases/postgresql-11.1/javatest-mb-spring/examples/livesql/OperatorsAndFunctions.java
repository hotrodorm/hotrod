package examples.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;

@Service("operatorsAndFunctionsExamples")
public class OperatorsAndFunctions {

  @Autowired
  private LiveSQL sql;

  public void general() {

    // eq(), ne(), lt(), le(), gt(), ge()
    // and(), or(), not()
    // like(), notLike()
    // isNull(), isNotNull(),
    // between(), notBetween()
    // coalesce()

    // tuple()
    // in(subquery), notIn(subquery), in(list), notIn(list)
    // eqAny(), neAny(), ltAny(), leAny(), gtAny(), geAny()
    // eqAll(), neAll(), ltAll(), leAll(), gtAll(), geAll()
    // exists(), notExists()

    // --- Aggregation expressions --
    // count(), countDistinct(), sumDistinct(), avgDistinct(),
    // groupConcatDistinct()

    // --- Aggregation expressions, that ALSO are window functions ---
    // sum(), avg(), min(), max(), groupConcat()
    // .over().partitionBy().orderBy().end()

    // --- Analytical functions ---
    // rowNumber(), rank(), denseRank(), ntile()
    // .over().partitionBy().orderBy().end()

    // --- Positional Analytic functions ---
    // lead(), lag()
    // .over().partitionBy().orderBy().end()

    // caseWhen().when().else().end()

  }

  public void generalPurpose() {

    AccountTable a = AccountDAO.newTable("a");

    // coalesce()

    // SELECT coalesce(coalesce(a.name, a.type), 'no name')
    // FROM account a

    List<Map<String, Object>> rows = sql //
        .select(a.name.coalesce(a.type).coalesce("no name")) //
        .from(a) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void arithmetic() {

    AccountTable a = AccountDAO.newTable("a");

    // plus(), minus(), mult(), div(), remainder()

    // SELECT current_balance * 1.17 as projectedBalance
    // FROM account a
    // WHERE ((a.current_balance + 10.2 - 0.75) * (a.id - 4) / 1.10) < 1.20

    List<Map<String, Object>> rows = sql //
        .select(a.currentBalance.mult(1.17).as("projectedBalance")) //
        .from(a) //
        .where(a.currentBalance.plus(10.2).minus(0.75).mult(a.id.minus(4)).div(1.10).le(1.20)) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void numericFunctions() {

    AccountTable a = AccountDAO.newTable("a");

    // log(), pow(), abs(), neg(), signum()

    // SELECT *
    // FROM account a
    // WHERE abs(power(-a.id, 2.5)) > 10

    List<Map<String, Object>> rows = sql //
        .select() //
        .from(a) //
        .where(a.id.neg().pow(2.5).abs().ge(10)) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void stringFunctions() {

    AccountTable a = AccountDAO.newTable("a");

    // concat(), length(), locate(), substring(), lower(), upper(), trim()

    // SELECT *
    // FROM account a
    // WHERE substr(a.name, 10, 5) like a.type || '%'

    List<Map<String, Object>> rows = sql //
        .select() //
        .from(a) //
        .where(a.name.substr(10, 5).like(a.type.concat("%"))) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void dateTimeFunctions() {

    AccountTable a = AccountDAO.newTable("a");

    // currentDate(), currentTime(), currentDateTime(), datetime(), date(),
    // time(), extract()

    // SELECT *
    // FROM account a
    // WHERE extract(day from a.created_on) > 25

    List<Map<String, Object>> rows = sql //
        .select() //
        .from(a) //
        .where(a.createdOn.extract(DateTimeField.DAY).gt(25)) //
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

}
