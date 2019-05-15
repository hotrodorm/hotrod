package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.Join;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;

public abstract class JoinRenderer {

  public abstract String renderJoinKeywords(Join join) throws UnsupportedLiveSQLFeatureException;

}
