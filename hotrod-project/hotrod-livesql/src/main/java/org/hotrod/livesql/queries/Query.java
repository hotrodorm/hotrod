package org.hotrod.livesql.queries;

public interface Query {

  public String getPreview();

  public String getPreview(boolean includeParameters);

}
