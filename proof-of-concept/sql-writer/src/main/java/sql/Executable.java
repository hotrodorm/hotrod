package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

public interface Executable {

  public List<Row> execute();

}
