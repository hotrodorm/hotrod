package org.hotrod.runtime.livesql.sysobjects;

import org.hotrod.runtime.livesql.expressions.TypeHandler;
import org.hotrod.runtime.livesql.metadata.Name;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.Table;

public class DualTable extends Table {

  // Properties

  public StringColumn dummy;

  // Constructors

  public DualTable() {
    super(null, null, Name.of("DUAL", false), "Table", null);
    initialize();
  }

  // Initialization

  private void initialize() {
    this.dummy = new StringColumn(this, "DUMMY", "dummy", "VARCHAR2", 1, 0, TypeHandler.STRING_TYPE_HANDLER);
    super.columns.add(this.dummy);
  }

}