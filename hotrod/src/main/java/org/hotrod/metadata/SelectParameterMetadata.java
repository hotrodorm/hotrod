package org.hotrod.metadata;

import java.io.Serializable;

import org.hotrod.config.ParameterTag;
import org.hotrod.utils.identifiers.Id;

public class SelectParameterMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

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
