package org.hotrod.livesql.sysobjects;

import org.hotrod.livesql.queries.InsertIntoPhase;
import org.hotrod.livesql.queries.InsertSetPhase;

public class NullInsertIntoPhase extends InsertIntoPhase<InsertSetPhase<?>> {

  public NullInsertIntoPhase() {
    super(null, null);
  }

}
