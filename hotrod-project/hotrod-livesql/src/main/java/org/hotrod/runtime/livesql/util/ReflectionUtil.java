package org.hotrod.runtime.livesql.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.Column;
import org.springframework.util.ReflectionUtils;

public class ReflectionUtil {

  public static List<Column> getColumnsField(final Object cs, final String colName)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      Field cf = ReflectionUtils.findField(cs.getClass(), colName);
//      System.out.println("cs (" + (cs == null ? "null" : cs.getClass().getName()) + ")");
      if (cf != null) {
        cf.setAccessible(true);
        Object object = cf.get(cs);
        @SuppressWarnings("unchecked")
        List<Column> columns = (List<Column>) object;
        return columns;
      } else {
        return new ArrayList<>();
      }
    } catch (ClassCastException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public static List<ResultSetColumn> getResultSetColumnsField(final Object cs, final String colName)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      Field cf = ReflectionUtils.findField(cs.getClass(), colName);
//      System.out.println("cs (" + (cs == null ? "null" : cs.getClass().getName()) + ")");
      if (cf != null) {
        cf.setAccessible(true);
        Object object = cf.get(cs);
        @SuppressWarnings("unchecked")
        List<ResultSetColumn> columns = (List<ResultSetColumn>) object;
        return columns;
      } else {
        return new ArrayList<>();
      }
    } catch (ClassCastException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public static String getStringField(final Object obj, final String property)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      Field f = ReflectionUtils.findField(obj.getClass(), property);
      if (f != null) {
        f.setAccessible(true);
        Object object = f.get(obj);
        String s = (String) object;
        return s;
      } else {
        throw new IllegalArgumentException("Could not find property '" + property + "' in object.");
      }
    } catch (ClassCastException e) {
      throw e;
    }
  }

  public static ComparableExpression getExpressionField(final Object obj, final String property)
      throws IllegalArgumentException, IllegalAccessException {
    try {
      Field f = ReflectionUtils.findField(obj.getClass(), property);
      if (f != null) {
        f.setAccessible(true);
        Object object = f.get(obj);
        ComparableExpression s = (ComparableExpression) object;
        return s;
      } else {
        throw new IllegalArgumentException("Could not find property '" + property + "' in object.");
      }
    } catch (ClassCastException e) {
      throw e;
    }
  }

}
