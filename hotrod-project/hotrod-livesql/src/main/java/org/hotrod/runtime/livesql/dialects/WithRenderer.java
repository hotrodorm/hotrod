package org.hotrod.runtime.livesql.dialects;

public interface WithRenderer {

  String render(final boolean hasRecursiveCTEs);

}
