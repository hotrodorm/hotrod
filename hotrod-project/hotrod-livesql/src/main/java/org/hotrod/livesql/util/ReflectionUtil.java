package org.hotrod.livesql.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.springframework.util.ReflectionUtils;

public class ReflectionUtil {

  private ReflectionUtil() {
  }

  public static List<EntityColumn> getColumnsField(final Object cs, final String colName)
      throws IllegalArgumentException, IllegalAccessException {
    Field cf = ReflectionUtils.findField(cs.getClass(), colName);
    if (cf != null) {
      cf.setAccessible(true);
      Object object = cf.get(cs);
      @SuppressWarnings("unchecked")
      List<EntityColumn> columns = (List<EntityColumn>) object;
      return columns;
    } else {
      return new ArrayList<>();
    }
  }

  public static List<SQLExpression> getResultSetColumnsField(final Object cs, final String colName)
      throws IllegalArgumentException, IllegalAccessException {
    Field cf = ReflectionUtils.findField(cs.getClass(), colName);
    if (cf != null) {
      cf.setAccessible(true);
      Object object = cf.get(cs);
      @SuppressWarnings("unchecked")
      List<SQLExpression> columns = (List<SQLExpression>) object;
      return columns;
    } else {
      return new ArrayList<>();
    }
  }

  public static String getStringField(final Object obj, final String property)
      throws IllegalArgumentException, IllegalAccessException {
    Field f = ReflectionUtils.findField(obj.getClass(), property);
    if (f != null) {
      f.setAccessible(true);
      Object object = f.get(obj);
      String s = (String) object;
      return s;
    } else {
      throw new IllegalArgumentException("Could not find property '" + property + "' in object.");
    }
  }

  public static ComparableExpression getExpressionField(final Object obj, final String property)
      throws IllegalArgumentException, IllegalAccessException {
    Field f = ReflectionUtils.findField(obj.getClass(), property);
    if (f != null) {
      f.setAccessible(true);
      Object object = f.get(obj);
      ComparableExpression s = (ComparableExpression) object;
      return s;
    } else {
      throw new IllegalArgumentException("Could not find property '" + property + "' in object.");
    }
  }

}
