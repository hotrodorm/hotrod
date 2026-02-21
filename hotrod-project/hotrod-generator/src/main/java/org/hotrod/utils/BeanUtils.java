package org.hotrod.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.identifiers.Id.NamePart;

public abstract class BeanUtils {

  private static final Logger log = Logger.getLogger(BeanUtils.class.getName());

  public static List<NamePart> splitJava(final String javaName) {

    List<NamePart> parts = new ArrayList<NamePart>();

    // null or empty

    if (javaName == null || javaName.isEmpty()) {
      return parts;
    }

    int lastStart = 0;

    // a -- resolved
    // A -- resolved
    // _ -- resolved
    // _B
    // _aB
    // a_aB
    // A_Bb
    // aBb
    // aaBb
    // AaBb
    // aaaBb
    // AaaBb
    // AAABb

    while (lastStart < javaName.length()) {
      log.fine(" - pos=" + lastStart);

      // Single char left

      if (javaName.length() - lastStart == 1) {
        log.fine(" - Single char left");
        parts.add(new NamePart(javaName.substring(lastStart).toLowerCase()));
        return parts;
      }

      // Multiple chars left

      boolean firstUpper = Character.isUpperCase(javaName.charAt(lastStart));
      boolean secondUpper = Character.isUpperCase(javaName.charAt(lastStart + 1));

      if (firstUpper && secondUpper) { // acronym mode (full upper)
        log.fine(" - acronym mode (full upper)");
        int pos = lastStart + 2;
        boolean goOn = true;
        while (goOn && pos < javaName.length()) {
          if (!Character.isUpperCase(javaName.charAt(pos))) {
            goOn = false;
          } else {
            pos++;
          }
        }
        if (goOn) {
          parts.add(new NamePart(javaName.substring(lastStart).toLowerCase(), true));
          lastStart = javaName.length();
        } else {
          parts.add(new NamePart(javaName.substring(lastStart, pos - 1).toLowerCase(), true));
          lastStart = pos - 1;
        }

      } else if (firstUpper && !secondUpper) { // class mode (upper + lower)
        log.fine(" - class mode (upper + lower)");
        int pos = lastStart + 2;
        boolean goOn = true;
        while (goOn && pos < javaName.length()) {
          if (Character.isUpperCase(javaName.charAt(pos))) {
            goOn = false;
          } else {
            pos++;
          }
        }
        if (goOn) {
          parts.add(new NamePart(javaName.substring(lastStart).toLowerCase()));
          lastStart = javaName.length();
        } else {
          parts.add(new NamePart(javaName.substring(lastStart, pos).toLowerCase()));
          lastStart = pos;
        }

      } else if (!firstUpper && secondUpper) { // single-letter - split!
        log.fine(" - single-letter - split!");
        parts.add(new NamePart(javaName.substring(lastStart, lastStart + 1).toLowerCase()));
        lastStart++;

      } else { // member mode (full lower)
        log.fine(" - member mode (full lower)");
        int pos = lastStart + 2;
        boolean goOn = true;
        while (goOn && pos < javaName.length()) {
          if (Character.isUpperCase(javaName.charAt(pos))) {
            goOn = false;
          } else {
            pos++;
          }
        }
        if (goOn) {
          parts.add(new NamePart(javaName.substring(lastStart).toLowerCase()));
          lastStart = javaName.length();
        } else {
          parts.add(new NamePart(javaName.substring(lastStart, pos).toLowerCase()));
          lastStart = pos;
        }
      }

    }

    return parts;
  }

  // Simpler implementation that doesn't flag acronyms

  public static String[] splitPropertyName(String propertyName) {
    if (propertyName == null || propertyName.isEmpty()) {
      return new String[0];
    }
    return propertyName.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");
  }

}
