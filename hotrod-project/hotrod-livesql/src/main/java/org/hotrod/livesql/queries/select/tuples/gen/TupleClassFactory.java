package org.hotrod.livesql.queries.select.tuples.gen;

public class TupleClassFactory {

  public static Class<?> getTuplesClass(int modelInstancesCount) {
    switch (modelInstancesCount) {
    case 1:
      return Tuple1.class;
    case 2:
      return Tuple2.class;
    case 3:
      return Tuple3.class;
    case 4:
      return Tuple4.class;
    case 5:
      return Tuple5.class;
    case 6:
      return Tuple6.class;
    case 7:
      return Tuple7.class;
    case 8:
      return Tuple8.class;
    case 9:
      return Tuple9.class;
    case 10:
      return Tuple10.class;
    case 11:
      return Tuple11.class;
    case 12:
      return Tuple12.class;
    case 13:
      return Tuple13.class;
    case 14:
      return Tuple14.class;
    case 15:
      return Tuple15.class;
    case 16:
      return Tuple16.class;
    case 17:
      return Tuple17.class;
    case 18:
      return Tuple18.class;
    case 19:
      return Tuple19.class;
    case 20:
      return Tuple20.class;
    case 21:
      return Tuple21.class;
    case 22:
      return Tuple22.class;
    case 23:
      return Tuple23.class;
    case 24:
      return Tuple24.class;
    case 25:
      return Tuple25.class;
    case 26:
      return Tuple26.class;
    default:
      throw new RuntimeException("Cannot find class for tuples: invalid instance count of " + modelInstancesCount);
    }
  }

}
