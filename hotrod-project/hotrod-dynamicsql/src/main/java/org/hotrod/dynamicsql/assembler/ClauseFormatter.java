package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.Utl;

public class ClauseFormatter {

  private String clause;
  private String prefix;
  private String suffix;
  private String value;

  public ClauseFormatter(String clause, String prefix, String suffix) {
    this.clause = clause;
    this.prefix = prefix;
    this.suffix = suffix;
    this.value = this.clause == null ? null
        : Utl.coalesce(this.prefix, "") + this.clause + Utl.coalesce(this.suffix, "");
  }

  public String getClause() {
    return clause;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getSuffix() {
    return suffix;
  }

  public String getValue() {
    return value;
  }

}
