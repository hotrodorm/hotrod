package tests;

import java.util.List;

import com.sun.rowset.internal.Row;

import gen.database.metadata.Employee;
import sql.SQL;

public class SelectWriter {

  public static void main(final String[] args) {

    Employee e = new Employee();
    Employee j = new Employee();

    List<Row> rows = SQL //
        .select(e.id, e.name, j.name) //
        .from(e) //
        .join(j, e.managerId.equals(j.id)) //
        .where(e.salary.greaterThan(SQL.constant(1000))) //
        .and(e.name.like(SQL.constant("SM%"))) //
        .or(e.salary.lessThan(j.salary)) //
        .groupBy(e.id) //
        .having(e.departmentId.between(SQL.constant(1000), SQL.constant(2000))) //
        .orderBy(e.departmentId.asc(), j.name.desc()) //
        .execute();
  }

}
