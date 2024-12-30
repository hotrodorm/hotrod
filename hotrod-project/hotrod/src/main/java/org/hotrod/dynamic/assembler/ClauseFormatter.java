package org.hotrod.dynamic.assembler;

import org.hotrod.utils.SUtil;

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
        : SUtil.coalesce(this.prefix, "") + this.clause + SUtil.coalesce(this.suffix, "");
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
