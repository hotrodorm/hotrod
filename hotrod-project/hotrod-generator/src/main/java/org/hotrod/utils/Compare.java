package org.hotrod.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SelectGenerationTag.SelectStrategy;
import org.hotrod.config.dynamicsql.DynamicSQLPart.ParameterDefinitions;
import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.ObjectId;

public class Compare {

  private static final Logger log = LogManager.getLogger(Compare.class);

  @SuppressWarnings("unchecked")
  public static boolean same(final List<?> a, final List<?> b) {
    log.debug("a[" + (a == null ? "null" : "" + a.size()) + "] =? b[" + (b == null ? "null" : "" + b.size()) + "]");
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
      log.debug("  correlating...");
      List<AbstractConfigurationTag> la = (List<AbstractConfigurationTag>) a;
      List<AbstractConfigurationTag> lb = (List<AbstractConfigurationTag>) b;
      Iterator<AbstractConfigurationTag> ita = la.iterator();
      Iterator<AbstractConfigurationTag> itb = lb.iterator();
      while (ita.hasNext() && itb.hasNext()) {
        Object ae = ita.next();
        Object be = itb.next();
        if (ae instanceof String) {
          return ae.equals((String) be);
        } else {
          AbstractConfigurationTag ta = (AbstractConfigurationTag) ae;
          AbstractConfigurationTag tb = (AbstractConfigurationTag) be;
          boolean same = ta.same(tb);
          log.debug("  [" + ta.getInternalCaption() + "] same=" + same);
          if (!same) {
            return false;
          }
        }
      }
      log.debug("  correlating finished 1");
      if (ita.hasNext() || itb.hasNext()) {
        log.debug("  correlating finished 2");
        return false;
      }
      log.debug("  correlating finished 3");
      return true;
    } catch (ClassCastException e) {
      log.error("  Invalid list! ", e);
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

  public static boolean same(final ObjectId a, final ObjectId b) {
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
  }

  public static boolean same(final Id a, final Id b) {
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
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

  public static boolean same(final SelectStrategy a, final SelectStrategy b) {
    if (a == null) {
      return b == null;
    }
    return a == b;
  }

  // Common types

  public static boolean same(final String a, final String b) {
    // log.info("a=" + a + " b=" + b);
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
  }

  public static boolean same(final boolean a, final boolean b) {
    return a == b;
  }

}
