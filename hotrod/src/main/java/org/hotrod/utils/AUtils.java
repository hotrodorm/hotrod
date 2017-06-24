package org.hotrod.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AUtils {

  public static <T> T[] concat(T[] first, T[] second, T[] zeroLengthArray) {
    List<T> both = new ArrayList<T>(first.length + second.length);
    Collections.addAll(both, first);
    Collections.addAll(both, second);
    return both.toArray(zeroLengthArray);
  }

}
