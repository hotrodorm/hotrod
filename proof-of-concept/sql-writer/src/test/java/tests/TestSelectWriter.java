package tests;

import java.util.List;

import com.sun.rowset.internal.Row;

import gen.database.metadata.Department;
import gen.database.metadata.Employee;
import gen.database.metadata.schema2.Vacation;
import sql.SQL;

public class TestSelectWriter {

  public static void main(final String[] args) {

    Employee e = new Employee("e");
    Employee j = new Employee("j");
    Department d = new Department("d");
    Vacation v = new Vacation("v");

    List<Row> rows = SQL //
        .select(e.id, e.name, j.name, SQL.count(), SQL.countDistinct(e.departmentId)) //
        .from(e) //
        .join(j, e.managerId.equals(j.id)) //
        .join(d, d.id.equals(e.departmentId)) //
        .leftJoin(v, v.employeeId.equals(e.id)) //
        .where(e.salary.greaterThan(SQL.constant(1000))) //
        .and(e.name.like(SQL.constant("SM%"))) //
        .or(e.salary.lessThan(j.salary)) //
        .groupBy(e.id) //
        .having(SQL.count().between(SQL.constant(1000), SQL.constant(2000))) //
        .orderBy(e.name.asc(), j.name.desc()) //
        .offset(300) //
        .limit(50) //
        .execute() //
    ;

  }

}
