package org.hotrod.livesql.dialects;

public interface WithRenderer {

  String render(final boolean hasRecursiveCTEs);

}
