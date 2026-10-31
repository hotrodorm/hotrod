package org.hotrod.livesql.sysobjects;

import org.hotrod.livesql.queries.UpdateTablePhase;

public class NullUpdateTablePhase extends UpdateTablePhase {

  public NullUpdateTablePhase() {
    super(null, null);
  }

}
