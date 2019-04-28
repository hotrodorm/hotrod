package sql;

import java.util.List;

import com.sun.rowset.internal.Row;

public interface ExecutableSelect {

  void renderTo(final QueryWriter w);

  List<Row> execute();

}
