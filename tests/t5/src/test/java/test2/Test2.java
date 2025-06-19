package test2;

import java.util.List;

import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.metadata.WrappingColumn;

public class Test2 {

  public static void main(String[] args) {

    Name cat = Name.parse("catalog1");
    Name sch = Name.parse("schema1");
    Name tab = Name.parse("table1");

    MyAccount ma = new MyAccount(cat, sch, tab, "TABLE", "a");

    List<Acc> list = Test2.get(ma);

  }

  public static class MyAccount extends Table<Acc> {

    public MyAccount(Name catalog, Name schema, Name name, String type, String alias) {
      super(catalog, schema, name, type, alias, Acc.class);
    }

    @Override
    protected WrappingColumn star() {
      return null;
    }

  }

  public static <T> List<T> get(Table<T> t) {
    return null;
  }

}
