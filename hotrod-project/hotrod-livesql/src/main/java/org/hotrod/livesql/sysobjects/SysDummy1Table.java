package org.hotrod.livesql.sysobjects;

import org.hotrod.livesql.metadata.AllColumns;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.metadata.WrappingColumn;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class SysDummy1Table extends Table<String> {

  // Properties

  public CharEntityColumn ibmreqd;

  // Constructors

  public SysDummy1Table() {
    super(null, Name.of("SYSIBM", false), Name.of("SYSDUMMY1", false), "Table", null, String.class);
    initialize();
  }

  // Initialization

  private void initialize() {
    this.ibmreqd = new CharEntityColumn(this, "IBMREQD", "ibmreqd", "VARCHAR", 1, 0,
        TypeHandler.STRING_ENTITY_COLUMN);
    super.add(this.ibmreqd);
  }

  @Override
  protected WrappingColumn star() {
    return new AllColumns(this.ibmreqd);
  }

}