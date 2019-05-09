package org.hotrod.runtime.livesql.metadata;

import java.util.Date;

public class DateTimeColumn extends Column<Date> {

  public DateTimeColumn(final TableOrView objectIntance, final String name, final String property) {
    super(objectIntance, name, property);
  }

}
