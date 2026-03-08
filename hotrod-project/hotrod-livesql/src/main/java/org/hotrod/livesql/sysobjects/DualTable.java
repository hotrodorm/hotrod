package org.hotrod.livesql.sysobjects;

import org.hotrod.livesql.metadata.AllColumns;
import org.hotrod.livesql.metadata.CharEntityColumn;
import org.hotrod.livesql.metadata.Name;
import org.hotrod.livesql.metadata.DirectEntityColumnMetaData;
import org.hotrod.livesql.metadata.SQLMetaExpression;
import org.hotrod.livesql.metadata.Table;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class DualTable extends Table<String> {

  // Properties

  public CharEntityColumn dummy;

  // Constructors

  public DualTable() {
    super(null, null, Name.of("DUAL", false), "Table", null, String.class, String.class);
    initialize();
  }

  // Initialization

  private void initialize() {
    DirectEntityColumnMetaData c = new DirectEntityColumnMetaData("DUMMY", "dummy", "VARCHAR2", 1, 0,
        TypeHandler.STRING_ENTITY_COLUMN);
    this.dummy = new CharEntityColumn(this, c);
    super.add(this.dummy);
  }

  @Override
  protected SQLMetaExpression star() {
    return new AllColumns(this.dummy);
  }

}