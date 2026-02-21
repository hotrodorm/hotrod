package org.hotrod.livesql.sysobjects;

import org.hotrod.livesql.metadata.AllColumns;
import org.hotrod.livesql.metadata.CharEntityColumnMetaData;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.SQLMetaExpression;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class SysDummy1Table extends Table<String> {

  // Properties

  public CharEntityColumn ibmreqd;

  // Constructors

  public SysDummy1Table() {
    super(null, Name.of("SYSIBM", false), Name.of("SYSDUMMY1", false), "Table", null, String.class, String.class);
    initialize();
  }

  // Initialization

  private void initialize() {
    CharEntityColumnMetaData c = new CharEntityColumnMetaData("IBMREQD", "ibmreqd", "VARCHAR", 1, 0, TypeHandler.STRING_ENTITY_COLUMN);
    this.ibmreqd = new CharEntityColumn(this, c);
    super.add(this.ibmreqd);
  }

  @Override
  protected SQLMetaExpression star() {
    return new AllColumns(this.ibmreqd);
  }

}