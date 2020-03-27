package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.queries.select.Join;

public abstract class JoinRenderer {

  public abstract String renderJoinKeywords(Join join) throws UnsupportedLiveSQLFeatureException;

}
