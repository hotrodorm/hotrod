package org.hotrod.metadata;

import org.hotrod.config.ParameterTag;
import org.hotrod.identifiers.Id;

public class SelectParameterMetadata {

  private ParameterTag p;

  public SelectParameterMetadata(final ParameterTag p) {
    this.p = p;
  }

  // Getters

  public Id getId() {
    return this.p.getId();
  }

  public ParameterTag getParameter() {
    return p;
  }

}
