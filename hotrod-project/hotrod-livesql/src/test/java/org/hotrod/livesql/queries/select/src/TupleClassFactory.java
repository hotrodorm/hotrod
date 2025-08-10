package org.hotrod.livesql.queries.select.src;

public class TupleClassFactory {

  public static Class<?> getTuplesClass(int modelInstancesCount) {
    switch (modelInstancesCount) {
    case 1:
      return Tuple1.class;
    case 2:
      return Tuple2.class;
    case 3:
      return Tuple3.class;
    default:
      throw new RuntimeException("Cannot find class for tuples: invalid instance count of " + modelInstancesCount);
    }
  }

}
