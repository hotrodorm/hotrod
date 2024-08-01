package t1;

import org.hotrod.runtime.livesql.queries.typesolver.ResultSetColumnMetadata;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;
import org.hotrod.runtime.livesql.queries.typesolver.TypeSolver;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;

public class T1 {

  public static void main(String[] args) throws UnresolvableDataTypeException {
    TypeSolver s = new TypeSolver();
    ResultSetColumnMetadata cm = null;
    TypeHandler th = s.resolve(cm);
  }

}
