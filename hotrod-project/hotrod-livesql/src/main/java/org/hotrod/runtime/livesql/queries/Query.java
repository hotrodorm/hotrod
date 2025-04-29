package org.hotrod.runtime.livesql.queries;

public interface Query {

  public String getPreview();

  public String getPreview(boolean includeParameters);

}
