package org.hotrod.runtime.livesql.sysobjects;

import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.Table;

public class DualTable extends Table {

  // Properties

  public StringColumn dummy;

  // Constructors

  public DualTable() {
    super(null, null, "DUAL", "Table", null);
    initialize();
  }

  // Initialization

  private void initialize() {
    this.dummy = new StringColumn(this, "DUMMY", "dummy");
  }

}