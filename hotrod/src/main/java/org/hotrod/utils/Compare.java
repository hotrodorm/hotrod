package org.hotrod.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;

public class Compare {

  private static final Logger log = Logger.getLogger(Compare.class);

  @SuppressWarnings("unchecked")
  public static boolean same(final List<?> a, final List<?> b) {
    if (a == null && b == null) {
      return true;
    }
    if (a != null && b == null) {
      return false;
    }
    if (a == null && b != null) {
      return false;
    }
    if (a.size() != b.size()) {
      return false;
    }
    try {
      List<AbstractConfigurationTag> la = (List<AbstractConfigurationTag>) a;
      List<AbstractConfigurationTag> lb = (List<AbstractConfigurationTag>) b;
      Iterator<AbstractConfigurationTag> ita = la.iterator();
      Iterator<AbstractConfigurationTag> itb = lb.iterator();
      while (ita.hasNext() && itb.hasNext()) {
        AbstractConfigurationTag ta = ita.next();
        AbstractConfigurationTag tb = itb.next();
        boolean same = ta.same(tb);
        log.debug("[" + ta.getTagName() + " / " + ta.getClass().getName() + "] same=" + same);
        if (!same) {
          return false;
        }
      }
      if (ita.hasNext() || itb.hasNext()) {
        return false;
      }
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  public static boolean same(final ParameterDefinitions a, final ParameterDefinitions b) {
    if (a == null || b == null) {
      return a == null && b == null;
    }
    List<ParameterTag> la = a.getDefinitions();
    List<ParameterTag> lb = b.getDefinitions();
    Iterator<ParameterTag> ita = la.iterator();
    Iterator<ParameterTag> itb = lb.iterator();
    while (ita.hasNext() && itb.hasNext()) {
      ParameterTag ta = ita.next();
      ParameterTag tb = itb.next();
      if (!ta.same(tb)) {
        return false;
      }
    }
    if (ita.hasNext() || itb.hasNext()) {
      return false;
    }
    return true;
  }

  public static boolean same(final AbstractConfigurationTag a, final AbstractConfigurationTag b) {
    if (a == null) {
      return b == null;
    }
    return a.same(b);
  }

  public static boolean same(final Map<String, ConverterTag> a, final Map<String, ConverterTag> b) {
    if (a == null || b == null) {
      return a == null && b == null;
    }
    return included(a, b) && included(b, a);
  }

  private static boolean included(final Map<String, ConverterTag> a, final Map<String, ConverterTag> b) {
    for (String key : a.keySet()) {
      AbstractConfigurationTag va = a.get(key);
      if (!b.containsKey(key)) {
        return false;
      }
      AbstractConfigurationTag vb = b.get(key);
      if (!same(va, vb)) {
        return false;
      }
    }
    return true;
  }

  // Common types

  public static boolean same(final String a, final String b) {
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
  }

  public static boolean same(final boolean a, final boolean b) {
    return a == b;
  }

}
