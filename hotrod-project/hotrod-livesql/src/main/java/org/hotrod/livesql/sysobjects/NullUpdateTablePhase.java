package org.hotrod.livesql.sysobjects;

import org.hotrod.livesql.queries.UpdateSetPhase;
import org.hotrod.livesql.queries.UpdateTablePhase;

public class NullUpdateTablePhase extends UpdateTablePhase<UpdateSetPhase<?>> {

  public NullUpdateTablePhase() {
    super(null, null);
  }

}
