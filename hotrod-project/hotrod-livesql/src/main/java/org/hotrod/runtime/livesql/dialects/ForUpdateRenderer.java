package org.hotrod.runtime.livesql.dialects;

public interface ForUpdateRenderer {

  String renderAfterFromClause();

  String renderAfterLimitClause();

}
