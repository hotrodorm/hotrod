package app.test.assembly;

import java.sql.Timestamp;
import java.util.List;

import app.test.base.Table;

public class Test {

  public static void main(String[] args) {
    SQL sql = new SQL();
    Table<Integer> t1 = new Table<Integer>();
    Table<String> t2 = new Table<String>();
    Table<Timestamp> t3 = new Table<Timestamp>();

    List<XTuple1<Integer>> rs1 = sql.selectTuples() //
        .from(t1) //
        .where() //
        .execute();

    List<XTuple2<Integer, String>> rs2 = sql.selectTuples() //
        .from(t1) //
        .join(t2) //
        .where() //
        .execute();

    for (XTuple2<Integer, String> r : rs2) {
      r.get1();
    }

    List<XTuple3<Integer, String, Timestamp>> rs3 = sql.selectTuples() //
        .from(t1) //
        .join(t2) //
        .join(t3) //
        .where() //
        .execute();

    for (XTuple3<Integer, String, Timestamp> r : rs3) {
      r.get1();
    }

    for (XTuple3<Integer, String, Timestamp> r : sql.selectTuples() //
        .from(t1) //
        .join(t2) //
        .join(t3) //
        .where() //
        .execute()) {
      r.get1();
    }

  }

}
