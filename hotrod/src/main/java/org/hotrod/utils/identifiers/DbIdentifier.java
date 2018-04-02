package org.hotrod.utils.identifiers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class DbIdentifier extends Identifier {

  private static final long serialVersionUID = 1L;

  private static Logger log = Logger.getLogger(DbIdentifier.class);

  /** Look for this separator in the db name */
  private static final char DB_SEPARATOR = '_';

  /** Use this string in java when a null chunk is found */
  private static final String SEPARATOR_CHUNK = "_";

  /** Use this char in java to escape non ASCII chars */
  private static final char ESCAPE_CHAR = '$';

  public DbIdentifier(final String name) {
    initializeSQLName(name);
  }

  private void initializeSQLName(final String name) {
    // 1. First we find the chunks.

    log.debug("name='" + name + "' split chunks=" + name.split("_").length);

    List<String> chk = new ArrayList<String>();
    int start = 0;
    int pos = 0;
    while (pos < name.length()) {
      if (name.charAt(pos) == DB_SEPARATOR) {
        chk.add(name.substring(start, pos));
        start = pos + 1;
      }
      pos++;
    }
    if (start < name.length()) {
      chk.add(name.substring(start));
    } else {
      chk.add("");
    }

    // 2. Then we process each chunk.

    log.debug("real chunks=" + chk.size());

    this.javaChunks = new String[chk.size()];
    this.dbChunks = new String[chk.size()];
    int i = 0;
    for (Iterator<String> it = chk.iterator(); it.hasNext();) {
      String chunk = it.next();
      if ((chunk == null) || (chunk.length() == 0)) {
        this.javaChunks[i] = SEPARATOR_CHUNK;
        this.dbChunks[i] = "" + DB_SEPARATOR;
      } else {
        this.javaChunks[i] = genJavaAsciiChunk(chunk, (i == 0), true);
        this.dbChunks[i] = genJavaAsciiChunk(chunk, (i == 0), false);
      }
      i++;
    }
    log.debug("chunks=" + javaChunks.length);

    this.SQLName = name;

  }

  public DbIdentifier(final String name, final String javaName) {
    if (javaName != null) {
      initializeCompleteName(name, javaName);
    } else {
      initializeSQLName(name);
    }
  }

  private void initializeCompleteName(final String name, final String javaName) {
    // DB chunks

    this.dbChunks = name.split("" + DB_SEPARATOR);

    // Java chunks

    if (javaName.length() <= 1) {

      this.javaChunks = new String[1];
      this.javaChunks[0] = javaName.toLowerCase();

    } else {

      List<String> jc = new ArrayList<String>();
      int iniToken = 0;
      int pos = 1;

      while (pos < javaName.length()) {
        if (Character.isLowerCase(javaName.charAt(pos - 1)) && Character.isUpperCase(javaName.charAt(pos))) {
          // lower to upper
          if (pos > iniToken) {
            // System.out.println("lower-to-UPPER: iniToken=" + iniToken + "
            // pos=" + pos);
            jc.add(javaName.substring(iniToken, pos).toLowerCase());
            iniToken = pos;
          }
        } else if (Character.isUpperCase(javaName.charAt(pos - 1)) && Character.isLowerCase(javaName.charAt(pos))) {
          // upper to lower
          if (pos > iniToken + 1) {
            // System.out.println("UPPER-to-lower: iniToken=" + iniToken + "
            // pos=" + pos);
            jc.add(javaName.substring(iniToken, pos - 1).toLowerCase());
            iniToken = pos - 1;
          }
        }
        pos++;
      }
      if (pos > iniToken) {
        // System.out.println("tail: iniToken=" + iniToken + " pos=" + pos);
        String ck = javaName.substring(iniToken, pos).toLowerCase();
        jc.add(ck);
      }

      this.javaChunks = jc.toArray(new String[0]);
      // System.out.println("javaName=" + javaName + " tokens=" +
      // this.javaChunks.length);
    }

    // Predefined values

    this.SQLName = name;
    this.javaClassName = this.capitalizeFirst(javaName);
    this.javaMemberName = this.uncapitalizeFirst(javaName);
  }

  private String genJavaAsciiChunk(final String chunk, final boolean isFirstChunk, final boolean forceLowerCase) {
    StringBuffer sb = new StringBuffer();
    int pos = 0;
    while (pos < chunk.length()) {
      char c = chunk.charAt(pos);
      char b = Character.toLowerCase(c);
      log.debug("c=" + (int) c + " lower=" + (int) b);

      if ((pos == 0) && isFirstChunk) {
        if (Character.isJavaIdentifierStart(c) && (ESCAPE_CHAR != c) && (c > 32) && (c < 128)) {
          log.debug("-> is start");
          if (forceLowerCase) {
            sb.append(Character.toLowerCase(c));
          } else {
            sb.append(c);
          }
        } else {
          log.debug("-> escape start");
          sb.append(escapeChar(c));
        }
      } else {
        if (Character.isJavaIdentifierPart(c) && (ESCAPE_CHAR != c) && (c > 32) && (c < 128)) {
          log.debug("-> is part");
          if (forceLowerCase) {
            sb.append(Character.toLowerCase(c));
          } else {
            sb.append(c);
          }
        } else {
          log.debug("-> escape part");
          sb.append(escapeChar(c));
        }
      }
      pos++;
      log.debug("sb=" + sb.toString());
    }
    return sb.toString();
  }

  private String escapeChar(final char c) {
    int n = c;
    return ESCAPE_CHAR + toHexa(n >> 8) + toHexa(n & 0xff);
  }

  private static final String[] HEXA_DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
      "e", "f" };

  private String toHexa(final int value) {
    return HEXA_DIGITS[value >> 4] + HEXA_DIGITS[value & 0x0f];
  }

}
