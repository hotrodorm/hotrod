package org.hotrod.livesql.sysobjects;

import org.hotrod.livesql.metadata.AllColumns;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.metadata.WrappingColumn;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class DualTable extends Table<String> {

  // Properties

  public CharEntityColumn dummy;

  // Constructors

  public DualTable() {
    super(null, null, Name.of("DUAL", false), "Table", null, String.class);
    initialize();
  }

  // Initialization

  private void initialize() {
    this.dummy = new CharEntityColumn(this, "DUMMY", "dummy", "VARCHAR2", 1, 0, TypeHandler.STRING_ENTITY_COLUMN);
    super.add(this.dummy);
  }

  @Override
  protected WrappingColumn star() {
    return new AllColumns(this.dummy);
  }

}