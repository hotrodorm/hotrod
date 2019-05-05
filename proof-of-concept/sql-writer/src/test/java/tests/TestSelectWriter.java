package tests;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.sql.SQL;
import org.hotrod.runtime.sql.expressions.datetime.DateTimeFieldExpression.DateTimeField;

import gen.database.metadata.Department;
import gen.database.metadata.Employee;
import gen.database.metadata.schema2.Vacation;

public class TestSelectWriter {

  public static void main(final String[] args) {

    Employee e = new Employee("e");
    Department d = new Department("d");
    Vacation v = new Vacation("v");
    Employee j = new Employee("j");

    Department d2 = new Department("d2");

    // QueryWriter w = new QueryWriter(new PostgreSQLDialect());
    // WindowExpression<Number> wf = SQL.rowNumber().over().partitionBy(e.id,
    // j.departmentId).orderBy(e.name.asc(), v.approved.desc()).end();
    // wf.renderTo(w);
    // PreparedQuery q = w.getPreparedQuery();
    // System.out.println("q=" + q.getSQL());

    List<Map<String, Object>> rows = SQL //
        .createSelect(e.id, e.name, j.name, SQL.count(), SQL.countDistinct(e.departmentId), //
            SQL.coalesce(e.name, j.name), //
            SQL.caseWhen(e.name.like("%AN%"), 1).when(e.name.isNull(), 2).elseValue(-1).end().as("segment"), //
            SQL.sum(e.salary).over().partitionBy(j.name, v.approved).orderBy(e.departmentId.asc(), d.name.desc()).end(), //
            SQL.rowNumber().over().partitionBy(e.id).orderBy(e.name.desc().nullsFirst()).end(), //
            SQL.currentDate().extract(DateTimeField.DAY)
        ) //
        .from(e) //
        .join(d, d.id.eq(e.departmentId)) //
        .leftJoin(v, v.employeeId.eq(e.id)) //
        .leftJoin(j, j.id.eq(e.managerId)) //
        .where(e.salary.gt(500)) //
        .or(d.name.like("R%")) //
        .and(d.name.like("O'HARA%")) //
        .or(e.salary.lt(j.salary)) //
        .and(SQL.tuple(e.departmentId, j.name).notIn( //
            SQL.createSubquery(d2.id, d2.name) //
                .from(d2) //
                .where(d2.active.eq(1)))) //
        .groupBy(e.id) //
        .having(SQL.count().between(1000, 2500.14)) //
        .orderBy(e.name.asc(), j.name.desc()) //
        .offset(300) //
        .limit(50) //
        .execute() //
    ;

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

  }

}
