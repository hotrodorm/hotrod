package org.hotrod.runtime.livesql.sysobjects;

import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.Table;

public class SysDummy1Table extends Table {

  // Properties

  public StringColumn ibmreqd;

  // Constructors

  public SysDummy1Table() {
    super(null, "SYSIBM", "SYSDUMMY1", "Table", null);
    initialize();
  }

  // Initialization

  private void initialize() {
    this.ibmreqd = new StringColumn(this, "IBMREQD", "ibmreqd", "VARCHAR", 1, 0);
  }

}