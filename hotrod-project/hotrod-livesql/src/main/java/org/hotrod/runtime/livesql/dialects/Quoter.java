package org.hotrod.runtime.livesql.dialects;

import java.util.regex.Pattern;

// This class quotes and escapes an identifier that needs quoting

public class Quoter {

  private String quotePrefix; // initial quote
  private String quoteSuffix; // end quote

  private String unescapedCharRegex; // determines if a quoted char does not need escaping
  private String escapableCharRegex; // determines if a char is still printable and can be escaped; otherwise an
                                     // error should be thrown or the char should be ignored
  private String escapePrefix; // the prefix when escaping a quoted char
  private String escapeSuffix;// the suffix when escaping a quoted char
  
  private Pattern unescapedCharPattern;
  private Pattern escapableCharPattern;

  public Quoter(final String quotePrefix, final String quoteSuffix, final String unescapedCharRegex,
      final String escapableCharRegex, final String escapePrefix, final String escapeSuffix) {
    this.quotePrefix = quotePrefix;
    this.quoteSuffix = quoteSuffix;
    this.unescapedCharRegex = unescapedCharRegex;
    this.escapableCharRegex = escapableCharRegex;
    this.escapePrefix = escapePrefix;
    this.escapeSuffix = escapeSuffix;
    
    this.unescapedCharPattern = Pattern.compile(this.unescapedCharRegex);
    this.escapableCharPattern = Pattern.compile(this.escapableCharRegex);
  }

  public String quote(final String id) {
    if (id == null)
      return null;
    StringBuilder sb = new StringBuilder();
    if (this.quotePrefix != null) {
      sb.append(this.quotePrefix);
    }
    for (int i = 0; i < id.length(); i++) {
      String c = id.substring(i, i + 1);
      if (this.unescapedCharPattern.matcher(c).matches()) {
        sb.append(c);
      } else if (this.escapableCharPattern.matcher(c).matches()) {
        sb.append(this.escapePrefix);
        sb.append(c);
        sb.append(this.escapeSuffix);
      } else {
        // For safety the unsafe character is removed from the SQL statement.
        // It's better than the SQL crashes and fails to run, than allowing SQL
        // Injection.
      }
    }
    if (this.quoteSuffix != null) {
      sb.append(this.quoteSuffix);
    }
    return sb.toString();
  }

}
