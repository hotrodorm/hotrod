package org.hotrod.runtime.sql.dialects;

import org.hotrod.runtime.sql.Join;
import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;

public abstract class JoinRenderer {

  public abstract String renderJoinKeywords(Join join) throws UnsupportedFeatureException;

}
