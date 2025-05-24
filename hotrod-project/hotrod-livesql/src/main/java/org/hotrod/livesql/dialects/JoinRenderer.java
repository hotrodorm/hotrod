package org.hotrod.livesql.dialects;

import org.hotrod.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.livesql.queries.select.Join;

public abstract class JoinRenderer {

  public abstract String renderJoinKeywords(Join join) throws UnsupportedLiveSQLFeatureException;

  public String renderOptionalOnPredicate(Join join) throws UnsupportedLiveSQLFeatureException {
    return "";
  }

}
