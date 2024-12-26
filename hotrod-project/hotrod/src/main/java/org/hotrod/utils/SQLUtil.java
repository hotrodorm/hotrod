package org.hotrod.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.interfaces.OrderBy;

public class SQLUtil {

  public static String render(OrderBy[] orderBies) {
    return (orderBies.length > 0 ? "\nORDER BY " : "") + Arrays.stream(orderBies)
        .map(o -> o.getSQLColumnName() + (o.isAscending() ? "" : " DESC")).collect(Collectors.joining(", "));
  }

}
