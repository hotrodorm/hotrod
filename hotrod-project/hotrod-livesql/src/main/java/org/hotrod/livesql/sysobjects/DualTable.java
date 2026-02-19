package org.hotrod.livesql.sysobjects;

import org.hotrod.livesql.metadata.AllColumns;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.metadata.CharEntityInstanceColumn;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.SQLMetaExpression;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class DualTable extends Table<String> {

  // Properties

  public CharEntityInstanceColumn dummy;

  // Constructors

  public DualTable() {
    super(null, null, Name.of("DUAL", false), "Table", null, String.class, String.class);
    initialize();
  }

  // Initialization

  private void initialize() {
    CharEntityColumn c = new CharEntityColumn("DUMMY", "dummy", "VARCHAR2", 1, 0, TypeHandler.STRING_ENTITY_COLUMN);
    this.dummy = new CharEntityInstanceColumn(this, c);
    super.add(this.dummy);
  }

  @Override
  protected SQLMetaExpression star() {
    return new AllColumns(this.dummy);
  }

}