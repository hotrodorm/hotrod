package org.hotrod.runtime.livesql.util;

import java.util.HashMap;
import java.util.Map;

public class IdUtil {

  private static Map<Integer, Integer> ids = new HashMap<>();

  public static int id(final Object o) {
    if (o == null) {
      return 0;
    }
    Integer hc = Integer.valueOf(System.identityHashCode(o));
    if (ids.containsKey(hc)) {
      return ids.get(hc);
    } else {
      int n = ids.size() + 1;
      ids.put(hc, n);
      return n;
    }
  }

}
