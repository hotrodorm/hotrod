package org.hotrod.runtime.livesql.sysobjects;

import org.hotrod.runtime.livesql.metadata.AllColumns;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.Table;
import org.hotrod.runtime.livesql.metadata.WrappingColumn;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;

public class SysDummy1Table extends Table {

  // Properties

  public StringColumn ibmreqd;

  // Constructors

  public SysDummy1Table() {
    super(null, Name.of("SYSIBM", false), Name.of("SYSDUMMY1", false), "Table", null);
    initialize();
  }

  // Initialization

  private void initialize() {
    this.ibmreqd = new StringColumn(this, "IBMREQD", "ibmreqd", "VARCHAR", 1, 0, TypeHandler.STRING_ENTITY_COLUMN);
    super.add(this.ibmreqd);
  }

  @Override
  protected WrappingColumn star() {
    return new AllColumns(this.ibmreqd);
  }

}