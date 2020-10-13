package org.hotrodorm.hotrod.names;

// LiveSQL mapper is now loaded as a mapper interface, not an XML file. Remove in the future.

@Deprecated
public class Names {

  public static final String LIVE_SQL_MAPPER_NAMESPACE = "livesql";
  public static final String LIVE_SQL_MAPPER_STATEMENT_NAME = "select";
  public static final String LIVE_SQL_MAPPER_STATEMENT = LIVE_SQL_MAPPER_NAMESPACE + "."
      + LIVE_SQL_MAPPER_STATEMENT_NAME;

}
